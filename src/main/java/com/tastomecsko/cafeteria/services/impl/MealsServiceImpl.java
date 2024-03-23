package com.tastomecsko.cafeteria.services.impl;

import com.tastomecsko.cafeteria.dto.jwt.JwtRequest;
import com.tastomecsko.cafeteria.dto.meals.*;
import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.Order;
import com.tastomecsko.cafeteria.entities.User;
import com.tastomecsko.cafeteria.entities.meals.*;
import com.tastomecsko.cafeteria.exception.meal.BadCreateMealRequestException;
import com.tastomecsko.cafeteria.exception.meal.BadDeleteMealRequestException;
import com.tastomecsko.cafeteria.exception.meal.MealNotFoundException;
import com.tastomecsko.cafeteria.exception.menu.BadCreateMenuDetailsException;
import com.tastomecsko.cafeteria.exception.menu.BadModifyMenuDetailsException;
import com.tastomecsko.cafeteria.exception.menu.MenuAlreadyExistsException;
import com.tastomecsko.cafeteria.exception.menu.MenuNotFoundException;
import com.tastomecsko.cafeteria.repository.MenuRepository;
import com.tastomecsko.cafeteria.repository.OrderRepository;
import com.tastomecsko.cafeteria.repository.UserRepository;
import com.tastomecsko.cafeteria.repository.meals.*;
import com.tastomecsko.cafeteria.services.JWTService;
import com.tastomecsko.cafeteria.services.MealsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MealsServiceImpl implements MealsService {

    private final MenuRepository menuRepository;
    private final JWTService jwtService;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final MondayMealRepository mondayMealRepository;
    private final TuesdayMealRepository tuesdayMealRepository;
    private final WednesdayMealRepository wednesdayMealRepository;
    private final ThursdayMealRepository thursdayMealRepository;
    private final FridayMealRepository fridayMealRepository;
    private final SaturdayMealRepository saturdayMealRepository;
    private final SundayMealRepository sundayMealRepository;
    
    private final Long SET_DATE_TO_DAY_END = 86399000L;

    public void createMenu(CreateMenuRequest request) {
        request.setAvailableTo(request.getAvailableTo() + SET_DATE_TO_DAY_END);
        request.setSelectionEnd(request.getSelectionEnd() + SET_DATE_TO_DAY_END);

        createMenuRequestIntegrityChecker(request);

        saveMenuTimeDetails(request, new Menu());
    }

    //region Create Menu helper functions
    private void createMenuRequestIntegrityChecker(CreateMenuRequest request) {
        if(new Date().after(new Date(request.getSelectionStart())))
            throw new BadCreateMenuDetailsException("Menu selection date set before current date");

        Optional<List<Menu>> menusWithDateConflict = menuRepository.findAllByAvailableFromAfterAndAvailableFromBeforeOrAvailableToAfterAndAvailableToBeforeOrAvailableFromOrAvailableToOrAvailableFromBeforeAndAvailableToAfter(
                new Date(request.getAvailableFrom()), new Date(request.getAvailableTo()), new Date(request.getAvailableFrom()),
                new Date(request.getAvailableTo()), new Date(request.getAvailableFrom()),
                new Date(request.getAvailableTo()), new Date(request.getAvailableFrom()), new Date(request.getAvailableFrom())
        );

        if(menusWithDateConflict.isPresent()) {
            List<Menu> conflictedMenus = menusWithDateConflict.get();
            if(!conflictedMenus.isEmpty()) {
                throw new MenuAlreadyExistsException("There is already a menu with the same starting date");
            }
        }

        if(!isMenuDTOFilledOut(request)) {
            throw new BadCreateMenuDetailsException("Cannot create menu due to missing time data");
        }

        if(new Date(request.getSelectionEnd()).before(new Date(request.getSelectionStart()))) {
            throw new BadCreateMenuDetailsException("Selection end date cannot be before selection start date");
        }

        if(new Date(request.getAvailableTo()).before(new Date(request.getAvailableFrom()))) {
            throw new BadCreateMenuDetailsException("Availability end date cannot be before availability star date");
        }

        if(new Date(request.getAvailableFrom()).before(new Date(request.getSelectionEnd()))) {
            throw new BadCreateMenuDetailsException("Selection period ends after availability period starts");
        }
    }

    private boolean isMenuDTOFilledOut(CreateMenuRequest request) {
        if(request.getAvailableFrom().toString().isBlank() || request.getAvailableFrom().toString().isEmpty())
            return false;
        if(request.getAvailableTo().toString().isBlank() || request.getAvailableTo().toString().isEmpty())
            return false;
        if(request.getSelectionStart().toString().isBlank() || request.getSelectionStart().toString().isEmpty())
            return false;
        if(request.getSelectionEnd().toString().isBlank() || request.getSelectionEnd().toString().isEmpty())
            return false;
        return true;
    }

    private void saveMenuTimeDetails(CreateMenuRequest request, Menu menu) {
        menu.setAvailableFrom(new Date(request.getAvailableFrom()));
        menu.setAvailableTo(new Date(request.getAvailableTo()));

        menu.setSelectionStart(new Date(request.getSelectionStart()));
        menu.setSelectionEnd(new Date(request.getSelectionEnd()));

        menuRepository.save(menu);
    }

    //endregion

    public void modifyMenu(ModifyMenuRequest request) {
        Optional<Menu> optionalMenu = menuRepository.findById(request.getMenuId());

        if(optionalMenu.isEmpty()) {
            throw new MenuNotFoundException("Menu not found with id: " + request.getMenuId());
        }

        Menu menuToModify = optionalMenu.get();

        if(menuToModify.getSelectionStart().before(new Date()))
            throw new BadModifyMenuDetailsException("Menu selection has started, you can't modify the menu anymore!");

        if(!request.getSelectionStart().toString().isEmpty() || !request.getSelectionStart().toString().isBlank()) {
            menuToModify.setSelectionStart(new Date(request.getSelectionStart()));
        }

        if(!request.getSelectionEnd().toString().isEmpty() || !request.getSelectionEnd().toString().isBlank()) {
            request.setSelectionEnd(request.getSelectionEnd() + SET_DATE_TO_DAY_END);
            menuToModify.setSelectionEnd(new Date(request.getSelectionEnd()));
        }

        if(!request.getAvailableFrom().toString().isEmpty() || !request.getAvailableFrom().toString().isBlank()) {
            menuToModify.setAvailableFrom(new Date(request.getAvailableFrom()));
        }

        if(!request.getAvailableTo().toString().isEmpty() || !request.getAvailableTo().toString().isBlank()) {
            request.setAvailableTo(request.getAvailableTo() + SET_DATE_TO_DAY_END);
            menuToModify.setAvailableTo(new Date(request.getAvailableTo()));
        }

        modifyMenuRequestIntegrityChecker(request, menuToModify);

        menuRepository.save(menuToModify);
    }

    private void modifyMenuRequestIntegrityChecker(ModifyMenuRequest request, Menu menu) {
        if(new Date().after(new Date(request.getSelectionStart())))
            throw new BadModifyMenuDetailsException("Menu selection date set before current date");

        Optional<List<Menu>> menusWithDateConflict = menuRepository.findAllByAvailableFromAfterAndAvailableFromBeforeOrAvailableToAfterAndAvailableToBeforeOrAvailableFromOrAvailableToOrAvailableFromBeforeAndAvailableToAfter(
                menu.getAvailableFrom(), menu.getAvailableTo(), menu.getAvailableFrom(), menu.getAvailableTo(), menu.getAvailableFrom(),
                menu.getAvailableTo(), menu.getAvailableFrom(), menu.getAvailableFrom()
        );

        if(menusWithDateConflict.isPresent()) {
            List<Menu> dateConflictMenuList = menusWithDateConflict.get();
            if(dateConflictMenuList.size() > 1) {
                throw new MenuAlreadyExistsException("There is an existing menu overlapping with the provided availability start date");
            }
            else {
                if(!dateConflictMenuList.get(0).getId().equals(menu.getId())) {
                    throw new MenuAlreadyExistsException("There is an existing menu overlapping with the start date");
                }
            }
        }

        if(new Date(request.getSelectionEnd()).before(new Date(request.getSelectionStart()))) {
            throw new BadModifyMenuDetailsException("Selection end date cannot be before selection start date");
        }

        if(new Date(request.getAvailableTo()).before(new Date(request.getAvailableFrom()))) {
            throw new BadModifyMenuDetailsException("Availability end date cannot be before availability star date");
        }

        if(new Date(request.getAvailableFrom()).before(new Date(request.getSelectionEnd()))) {
            throw new BadModifyMenuDetailsException("Selection period ends after availability starts");
        }
    }

    public void deleteMenu(Integer id) {
        Optional<Menu> optionalMenu = menuRepository.findById(id);

        if(optionalMenu.isEmpty())
            throw new MenuNotFoundException("Menu not found with id: " + id);

        Menu menuToDelete = optionalMenu.get();

        List<MondayMeal> mondayMealList = menuToDelete.getMondayMeals();
        List<TuesdayMeal> tuesdayMealList = menuToDelete.getTuesdayMeals();
        List<WednesdayMeal> wednesdayMealList = menuToDelete.getWednesdayMeals();
        List<ThursdayMeal> thursdayMealList = menuToDelete.getThursdayMeals();
        List<FridayMeal> fridayMealList = menuToDelete.getFridayMeals();
        List<SaturdayMeal> saturdayMealList = menuToDelete.getSaturdayMeals();
        List<SundayMeal> sundayMealList = menuToDelete.getSundayMeals();

        for(MondayMeal mondayMeal : mondayMealList)
            mondayMealRepository.delete(mondayMeal);

        for(TuesdayMeal tuesdayMeal : tuesdayMealList)
            tuesdayMealRepository.delete(tuesdayMeal);

        for(WednesdayMeal wednesdayMeal : wednesdayMealList)
            wednesdayMealRepository.delete(wednesdayMeal);

        for(ThursdayMeal thursdayMeal : thursdayMealList)
            thursdayMealRepository.delete(thursdayMeal);

        for(FridayMeal fridayMeal : fridayMealList)
            fridayMealRepository.delete(fridayMeal);

        for(SaturdayMeal saturdayMeal : saturdayMealList)
            saturdayMealRepository.delete(saturdayMeal);

        for(SundayMeal sundayMeal : sundayMealList)
            sundayMealRepository.delete(sundayMeal);

        menuRepository.delete(menuToDelete);
    }

    public List<MenuTimeDetailResponse> getAllMenus() {
        List<MenuTimeDetailResponse> menuResponseList = new ArrayList<>();

        List<Menu> menuList = menuRepository.findAll();

        for (Menu menu : menuList) {
            MenuTimeDetailResponse response = new MenuTimeDetailResponse();

            response.setMenuId(menu.getId());

            response.setAvailableFrom(menu.getAvailableFrom().getTime());
            response.setAvailableTo(menu.getAvailableTo().getTime());

            response.setSelectionStart(menu.getSelectionStart().getTime());
            response.setSelectionEnd(menu.getSelectionEnd().getTime());

            menuResponseList.add(response);
        }

        return menuResponseList;
    }

    public List<MenuTimeDetailResponse> getCurrentMenusToSelect() {
        List<MenuTimeDetailResponse> responseList = new ArrayList<>();
        List<Menu> currentMenuList = menuRepository.findAllBySelectionStartBeforeAndSelectionEndAfter(new Date(), new Date());

        for(Menu menu : currentMenuList) {
            MenuTimeDetailResponse response = new MenuTimeDetailResponse();

            response.setMenuId(menu.getId());

            response.setAvailableFrom(menu.getAvailableFrom().getTime());
            response.setAvailableTo(menu.getAvailableTo().getTime());

            response.setSelectionStart(menu.getSelectionStart().getTime());
            response.setSelectionEnd(menu.getSelectionEnd().getTime());

            responseList.add(response);
        }

        return responseList;
    }

    public MenuResponse getMenuById(Integer menuId) {
        MenuResponse response = new MenuResponse();
        Optional<Menu> optionalCurrentMenu = menuRepository.findById(menuId);

        if(optionalCurrentMenu.isEmpty())
            throw new MenuNotFoundException("No menu found with the provided id: " + menuId);

        Menu currentMenu = optionalCurrentMenu.get();

        return getMenuResponse(response, currentMenu);
    }

    //region Get menu helper functions
    private MenuResponse getMenuResponse(MenuResponse response, Menu currentMenu) {
        response.setMenuId(currentMenu.getId());

        response.setAvailableFrom(currentMenu.getAvailableFrom().getTime());
        response.setAvailableTo(currentMenu.getAvailableTo().getTime());

        response.setSelectionStart(currentMenu.getSelectionStart().getTime());
        response.setSelectionEnd(currentMenu.getSelectionEnd().getTime());

        response.setMondayMeals(getMondayMeals(currentMenu.getMondayMeals()));
        response.setTuesdayMeals(getTuesdayMeals(currentMenu.getTuesdayMeals()));
        response.setWednesdayMeals(getWednesdayMeals(currentMenu.getWednesdayMeals()));
        response.setThursdayMeals(getThursdayMeals(currentMenu.getThursdayMeals()));
        response.setFridayMeals(getFridayMeals(currentMenu.getFridayMeals()));
        response.setSaturdayMeals(getSaturdayMeals(currentMenu.getSaturdayMeals()));
        response.setSundayMeals(getSundayMeals(currentMenu.getSundayMeals()));

        return response;
    }

    private List<MealResponse> getMondayMeals(List<MondayMeal> mealList) {
        List<MealResponse> responseList = new ArrayList<>();
        for (BaseMeal meal : mealList) {
            baseMealToMealResponse(meal, responseList);
        }
        return responseList;
    }

    private List<MealResponse> getTuesdayMeals(List<TuesdayMeal> mealList) {
        List<MealResponse> responseList = new ArrayList<>();
        for (BaseMeal meal : mealList) {
            baseMealToMealResponse(meal, responseList);
        }
        return responseList;
    }

    private List<MealResponse> getWednesdayMeals(List<WednesdayMeal> mealList) {
        List<MealResponse> responseList = new ArrayList<>();
        for (BaseMeal meal : mealList) {
            baseMealToMealResponse(meal, responseList);
        }
        return responseList;
    }

    private List<MealResponse> getThursdayMeals(List<ThursdayMeal> mealList) {
        List<MealResponse> responseList = new ArrayList<>();
        for (BaseMeal meal : mealList) {
            baseMealToMealResponse(meal, responseList);
        }
        return responseList;
    }

    private List<MealResponse> getFridayMeals(List<FridayMeal> mealList) {
        List<MealResponse> responseList = new ArrayList<>();
        for (BaseMeal meal : mealList) {
            baseMealToMealResponse(meal, responseList);
        }
        return responseList;
    }

    private List<MealResponse> getSaturdayMeals(List<SaturdayMeal> mealList) {
        List<MealResponse> responseList = new ArrayList<>();
        for (BaseMeal meal : mealList) {
            baseMealToMealResponse(meal, responseList);
        }
        return responseList;
    }

    private List<MealResponse> getSundayMeals(List<SundayMeal> mealList) {
        List<MealResponse> responseList = new ArrayList<>();
        for (BaseMeal meal : mealList) {
            baseMealToMealResponse(meal, responseList);
        }
        return responseList;
    }

    private static void baseMealToMealResponse(BaseMeal meal, List<MealResponse> responseList) {
        MealResponse response = new MealResponse();

        response.setMealId(meal.getId());
        response.setIdentification(meal.getIdentification());
        response.setDescription(meal.getDescription());

        responseList.add(response);
    }
    //endregion

    public MenuResponse createMeal(CreateMealRequest request) {
        if(request.getDayId() == null)
            throw new BadCreateMealRequestException("Unable to identify day of meal!");

        Optional<Menu> optionalMenu = menuRepository.findById(request.getMenuId());

        if(optionalMenu.isEmpty())
            throw new MenuNotFoundException("Menu not found with the provided id");

        Menu menu = optionalMenu.get();

        if(menu.getSelectionStart().before(new Date()))
            throw new BadCreateMealRequestException("Menu selection has started, you can't modify the meals anymore!");

        switch (request.getDayId()) {
            case 0: {
                MondayMeal meal = new MondayMeal();
                List<MondayMeal> mondayMealList = menu.getMondayMeals();

                meal.setMenu(menu);
                meal.setDescription(request.getDescription());
                meal.setIdentification(request.getIdentification());

                mondayMealList.add(meal);

                menu.setMondayMeals(mondayMealList);

                Menu savedMenu = menuRepository.save(menu);
                mondayMealRepository.save(meal);
                return menuToMenuResponseDTO(savedMenu);
            }
            case 1: {
                TuesdayMeal meal = new TuesdayMeal();
                List<TuesdayMeal> tuesdayMealList = menu.getTuesdayMeals();

                meal.setMenu(menu);
                meal.setDescription(request.getDescription());
                meal.setIdentification(request.getIdentification());

                tuesdayMealList.add(meal);

                menu.setTuesdayMeals(tuesdayMealList);

                Menu savedMenu = menuRepository.save(menu);
                tuesdayMealRepository.save(meal);
                return menuToMenuResponseDTO(savedMenu);
            }
            case 2: {
                WednesdayMeal meal = new WednesdayMeal();
                List<WednesdayMeal> wednesdayMealList = menu.getWednesdayMeals();

                meal.setMenu(menu);
                meal.setDescription(request.getDescription());
                meal.setIdentification(request.getIdentification());

                wednesdayMealList.add(meal);

                menu.setWednesdayMeals(wednesdayMealList);

                Menu savedMenu = menuRepository.save(menu);
                wednesdayMealRepository.save(meal);
                return menuToMenuResponseDTO(savedMenu);
            }
            case 3: {
                ThursdayMeal meal = new ThursdayMeal();
                List<ThursdayMeal> thursdayMealList = menu.getThursdayMeals();

                meal.setMenu(menu);
                meal.setDescription(request.getDescription());
                meal.setIdentification(request.getIdentification());

                thursdayMealList.add(meal);

                menu.setThursdayMeals(thursdayMealList);

                Menu savedMenu = menuRepository.save(menu);
                thursdayMealRepository.save(meal);
                return menuToMenuResponseDTO(savedMenu);
            }
            case 4: {
                FridayMeal meal = new FridayMeal();
                List<FridayMeal> fridayMealList = menu.getFridayMeals();

                meal.setMenu(menu);
                meal.setDescription(request.getDescription());
                meal.setIdentification(request.getIdentification());

                fridayMealList.add(meal);

                menu.setFridayMeals(fridayMealList);

                Menu savedMenu = menuRepository.save(menu);
                fridayMealRepository.save(meal);
                return menuToMenuResponseDTO(savedMenu);
            }
            case 5: {
                SaturdayMeal meal = new SaturdayMeal();
                List<SaturdayMeal> saturdayMealList = menu.getSaturdayMeals();

                meal.setMenu(menu);
                meal.setDescription(request.getDescription());
                meal.setIdentification(request.getIdentification());

                saturdayMealList.add(meal);

                menu.setSaturdayMeals(saturdayMealList);

                Menu savedMenu = menuRepository.save(menu);
                saturdayMealRepository.save(meal);
                return menuToMenuResponseDTO(savedMenu);
            }
            case 6: {
                SundayMeal meal = new SundayMeal();
                List<SundayMeal> sundayMealList = menu.getSundayMeals();

                meal.setMenu(menu);
                meal.setDescription(request.getDescription());
                meal.setIdentification(request.getIdentification());

                sundayMealList.add(meal);

                menu.setSundayMeals(sundayMealList);

                Menu savedMenu = menuRepository.save(menu);
                sundayMealRepository.save(meal);
                return menuToMenuResponseDTO(savedMenu);
            }
        }
        return menuToMenuResponseDTO(menu);
    }

    private MenuResponse menuToMenuResponseDTO(Menu menu) {
        MenuResponse response = new MenuResponse();

        response.setMenuId(menu.getId());
        response.setSelectionStart(menu.getSelectionStart().getTime());
        response.setSelectionEnd(menu.getSelectionEnd().getTime());
        response.setAvailableFrom(menu.getAvailableFrom().getTime());
        response.setAvailableTo(menu.getAvailableTo().getTime());
        response.setMondayMeals(getMondayMeals(menu.getMondayMeals()));
        response.setTuesdayMeals(getTuesdayMeals(menu.getTuesdayMeals()));
        response.setWednesdayMeals(getWednesdayMeals(menu.getWednesdayMeals()));
        response.setThursdayMeals(getThursdayMeals(menu.getThursdayMeals()));
        response.setFridayMeals(getFridayMeals(menu.getFridayMeals()));
        response.setSaturdayMeals(getSaturdayMeals(menu.getSaturdayMeals()));
        response.setSundayMeals(getSundayMeals(menu.getSundayMeals()));

        return  response;
    }

    public MenuResponse deleteMeal(DeleteMealRequest request) {
        if(request.getDayId() == null)
            throw new BadDeleteMealRequestException("Can't find day id");

        Optional<Menu> optionalMenu = menuRepository.findById(request.getMenuId());

        if(optionalMenu.isEmpty())
            throw new MenuNotFoundException("No menu found with the provided id");

        Menu menu = optionalMenu.get();

        if(menu.getSelectionStart().before(new Date()))
            throw new BadDeleteMealRequestException("Menu selection has started, you can't modify the meals anymore!");

        switch (request.getDayId()) {
            case 0: {
                try {
                    mondayMealRepository.deleteById(request.getMealId());
                    menu.setMondayMeals(mondayMealRepository.findAllByMenu(menu));
                    return menuToMenuResponseDTO(menuRepository.save(menu));
                }
                catch (Exception exception) {
                    throw new BadDeleteMealRequestException("Meal not found with the provided id: " + request.getMealId());
                }
            }
            case 1: {
                try {
                    tuesdayMealRepository.deleteById(request.getMealId());
                    menu.setTuesdayMeals(tuesdayMealRepository.findAllByMenu(menu));
                    return menuToMenuResponseDTO(menuRepository.save(menu));
                }
                catch (Exception exception) {
                    throw new BadDeleteMealRequestException("Meal not found with the provided id: " + request.getMealId());
                }
            }
            case 2: {
                try {
                    wednesdayMealRepository.deleteById(request.getMealId());
                    menu.setWednesdayMeals(wednesdayMealRepository.findAllByMenu(menu));
                    return menuToMenuResponseDTO(menuRepository.save(menu));
                }
                catch (Exception exception) {
                    throw new BadDeleteMealRequestException("Meal not found with the provided id: " + request.getMealId());
                }
            }
            case 3: {
                try {
                    thursdayMealRepository.deleteById(request.getMealId());
                    menu.setThursdayMeals(thursdayMealRepository.findAllByMenu(menu));
                    return menuToMenuResponseDTO(menuRepository.save(menu));
                }
                catch (Exception exception) {
                    throw new BadDeleteMealRequestException("Meal not found with the provided id: " + request.getMealId());
                }
            }
            case 4: {
                try {
                    fridayMealRepository.deleteById(request.getMealId());
                    menu.setFridayMeals(fridayMealRepository.findAllByMenu(menu));
                    return menuToMenuResponseDTO(menuRepository.save(menu));
                }
                catch (Exception exception) {
                    throw new BadDeleteMealRequestException("Meal not found with the provided id: " + request.getMealId());
                }
            }
            case 5: {
                try {
                    saturdayMealRepository.deleteById(request.getMealId());
                    menu.setSaturdayMeals(saturdayMealRepository.findAllByMenu(menu));
                    return menuToMenuResponseDTO(menuRepository.save(menu));
                }
                catch (Exception exception) {
                    throw new BadDeleteMealRequestException("Meal not found with the provided id: " + request.getMealId());
                }
            }
            case 6: {
                try {
                    sundayMealRepository.deleteById(request.getMealId());
                    menu.setSundayMeals(sundayMealRepository.findAllByMenu(menu));
                    return menuToMenuResponseDTO(menuRepository.save(menu));
                }
                catch (Exception exception) {
                    throw new BadDeleteMealRequestException("Meal not found with the provided id: " + request.getMealId());
                }
            }
        }
        return menuToMenuResponseDTO(menuRepository.save(menu));
    }

    public void createOrder(CreateOrderRequest request) {
        String userEmail = jwtService.extractUserName(request.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        Menu menu = menuRepository.findById(request.getMenuId()).orElseThrow(() ->
                new MenuNotFoundException("No menu found with the provided id"));

        Order order = new Order();

        List<Order> allOrders = orderRepository.findAllByAvailableFrom(menu.getAvailableFrom());

        for(Order currentOrder : allOrders) {
            if(currentOrder.getUser().equals(user))
                order = currentOrder;
        }

        MondayMeal mondayMealToSave = new MondayMeal();
        TuesdayMeal tuesdayMealToSave = new TuesdayMeal();
        WednesdayMeal wednesdayMealToSave = new WednesdayMeal();
        ThursdayMeal thursdayMealToSave = new ThursdayMeal();
        FridayMeal fridayMealToSave = new FridayMeal();
        SaturdayMeal saturdayMealToSave = new SaturdayMeal();
        SundayMeal sundayMealToSave = new SundayMeal();

        order.setAvailableFrom(menu.getAvailableFrom());
        order.setAvailableTo(menu.getAvailableTo());

        order.setUser(user);

        if(request.getMondayMealId() != null) {
            mondayMealToSave = mondayMealRepository.findById(request.getMondayMealId()).orElseThrow(() ->
                    new MealNotFoundException("No meal found with the provided id"));
            List<MondayMeal> mondayMealList = new ArrayList<>();
            mondayMealList.add(mondayMealToSave);
            order.setMondayMeal(mondayMealList);
        }
        if(request.getTuesdayMealId() != null) {
            tuesdayMealToSave = tuesdayMealRepository.findById(request.getTuesdayMealId()).orElseThrow(() ->
                    new MealNotFoundException("No meal found with the provided id"));

            List<TuesdayMeal> tuesdayMealList = new ArrayList<>();
            tuesdayMealList.add(tuesdayMealToSave);
            order.setTuesdayMeal(tuesdayMealList);
        }
        if(request.getWednesdayMealId() != null) {
            wednesdayMealToSave = wednesdayMealRepository.findById(request.getWednesdayMealId()).orElseThrow(() ->
                    new MealNotFoundException("No meal found with the provided id"));

            List<WednesdayMeal> wednesdayMealList = new ArrayList<>();
            wednesdayMealList.add(wednesdayMealToSave);
            order.setWednesdayMeal(wednesdayMealList);
        }
        if(request.getThursdayMealId() != null) {
            thursdayMealToSave = thursdayMealRepository.findById(request.getThursdayMealId()).orElseThrow(() ->
                    new MealNotFoundException("No meal found with the provided id"));

            List<ThursdayMeal> thursdayMealList = new ArrayList<>();
            thursdayMealList.add(thursdayMealToSave);
            order.setThursdayMeal(thursdayMealList);
        }
        if(request.getFridayMealId() != null) {
            fridayMealToSave = fridayMealRepository.findById(request.getFridayMealId()).orElseThrow(() ->
                    new MealNotFoundException("No meal found with the provided id"));

            List<FridayMeal> fridayMealList = new ArrayList<>();
            fridayMealList.add(fridayMealToSave);
            order.setFridayMeal(fridayMealList);
        }
        if(request.getSaturdayMealId() != null) {
            saturdayMealToSave = saturdayMealRepository.findById(request.getSaturdayMealId()).orElseThrow(() ->
                    new MealNotFoundException("No meal found with the provided id"));

            List<SaturdayMeal> saturdayMealList = new ArrayList<>();
            saturdayMealList.add(saturdayMealToSave);
            order.setSaturdayMeal(saturdayMealList);
        }
        if(request.getSundayMealId() != null) {
            sundayMealToSave = sundayMealRepository.findById(request.getSundayMealId()).orElseThrow(() ->
                    new MealNotFoundException("No meal found with the provided id"));

            List<SundayMeal> sundayMealList = new ArrayList<>();
            sundayMealList.add(sundayMealToSave);
            order.setSundayMeal(sundayMealList);
        }

        if(mondayMealToSave.getDescription() != null) {
            List<Order> orderList = mondayMealToSave.getUser_order();
            orderList.add(order);
            mondayMealToSave.setUser_order(orderList);
        }
        if(tuesdayMealToSave.getDescription() != null) {
            List<Order> orderList = tuesdayMealToSave.getUser_order();
            orderList.add(order);
            tuesdayMealToSave.setUser_order(orderList);
        }
        if(wednesdayMealToSave.getDescription() != null) {
            List<Order> orderList = wednesdayMealToSave.getUser_order();
            orderList.add(order);
            wednesdayMealToSave.setUser_order(orderList);
        }
        if(thursdayMealToSave.getDescription() != null) {
            List<Order> orderList = thursdayMealToSave.getUser_order();
            orderList.add(order);
            thursdayMealToSave.setUser_order(orderList);
        }
        if(fridayMealToSave.getDescription() != null) {
            List<Order> orderList = fridayMealToSave.getUser_order();
            orderList.add(order);
            fridayMealToSave.setUser_order(orderList);
        }
        if(saturdayMealToSave.getDescription() != null) {
            List<Order> orderList = saturdayMealToSave.getUser_order();
            orderList.add(order);
            saturdayMealToSave.setUser_order(orderList);
        }
        if(sundayMealToSave.getDescription() != null) {
            List<Order> orderList = sundayMealToSave.getUser_order();
            orderList.add(order);
            sundayMealToSave.setUser_order(orderList);
        }

        orderRepository.save(order);
    }

    public void createTestData() {
        CreateMenuRequest request = new CreateMenuRequest();

        request.setSelectionStart(1710889200000L);
        request.setSelectionEnd(1711321199000L);
        request.setAvailableFrom(1711321200000L);
        request.setAvailableTo(1711922399000L);

        Menu menu = new Menu();

        menu.setAvailableFrom(new Date(request.getAvailableFrom()));
        menu.setAvailableTo(new Date(request.getAvailableTo()));

        menu.setSelectionStart(new Date(request.getSelectionStart()));
        menu.setSelectionEnd(new Date(request.getSelectionEnd()));

        menuRepository.save(menu);
    }

    public MealResponse getMealForToday(JwtRequest request) {
        MealResponse mealResponse = new MealResponse();

        String userEmail = jwtService.extractUserName(request.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        List<Order> userOrders = user.getOrders();
        Order orderForWeek = new Order();

        for(Order order : userOrders) {
            if(order.getAvailableFrom().after(new Date()) && order.getAvailableTo().before(new Date())) {
                orderForWeek = order;
            }
        }

        switch (LocalDate.now().getDayOfWeek()) {
            case MONDAY: {
                mealResponse.setIdentification(orderForWeek.getMondayMeal().get(0).getIdentification());
                mealResponse.setDescription(orderForWeek.getMondayMeal().get(0).getDescription());
                break;
            }
            case TUESDAY: {
                mealResponse.setIdentification(orderForWeek.getTuesdayMeal().get(0).getIdentification());
                mealResponse.setDescription(orderForWeek.getTuesdayMeal().get(0).getDescription());
                break;
            }
            case WEDNESDAY: {
                mealResponse.setIdentification(orderForWeek.getWednesdayMeal().get(0).getIdentification());
                mealResponse.setDescription(orderForWeek.getWednesdayMeal().get(0).getDescription());
                break;
            }
            case THURSDAY: {
                mealResponse.setIdentification(orderForWeek.getThursdayMeal().get(0).getIdentification());
                mealResponse.setDescription(orderForWeek.getThursdayMeal().get(0).getDescription());
                break;
            }
            case FRIDAY: {
                mealResponse.setIdentification(orderForWeek.getFridayMeal().get(0).getIdentification());
                mealResponse.setDescription(orderForWeek.getFridayMeal().get(0).getDescription());
                break;
            }
            case SATURDAY: {
                mealResponse.setIdentification(orderForWeek.getSaturdayMeal().get(0).getIdentification());
                mealResponse.setDescription(orderForWeek.getSaturdayMeal().get(0).getDescription());
                break;
            }
            case SUNDAY: {
                mealResponse.setIdentification(orderForWeek.getSundayMeal().get(0).getIdentification());
                mealResponse.setDescription(orderForWeek.getSundayMeal().get(0).getDescription());
                break;
            }
        }

        return mealResponse;
    }
}