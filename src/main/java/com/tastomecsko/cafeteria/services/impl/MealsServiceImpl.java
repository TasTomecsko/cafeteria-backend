package com.tastomecsko.cafeteria.services.impl;

import com.tastomecsko.cafeteria.dto.jwt.JwtRequest;
import com.tastomecsko.cafeteria.dto.meals.*;
import com.tastomecsko.cafeteria.entities.Meal;
import com.tastomecsko.cafeteria.entities.Menu;
import com.tastomecsko.cafeteria.entities.Order;
import com.tastomecsko.cafeteria.entities.User;
import com.tastomecsko.cafeteria.entities.comparator.MealComparator;
import com.tastomecsko.cafeteria.exception.meal.BadCreateMealRequestException;
import com.tastomecsko.cafeteria.exception.meal.BadDeleteMealRequestException;
import com.tastomecsko.cafeteria.exception.meal.MealNotFoundException;
import com.tastomecsko.cafeteria.exception.menu.BadCreateMenuDetailsException;
import com.tastomecsko.cafeteria.exception.menu.BadModifyMenuDetailsException;
import com.tastomecsko.cafeteria.exception.menu.MenuAlreadyExistsException;
import com.tastomecsko.cafeteria.exception.menu.MenuNotFoundException;
import com.tastomecsko.cafeteria.repository.MealRepository;
import com.tastomecsko.cafeteria.repository.MenuRepository;
import com.tastomecsko.cafeteria.repository.OrderRepository;
import com.tastomecsko.cafeteria.repository.UserRepository;
import com.tastomecsko.cafeteria.services.JWTService;
import com.tastomecsko.cafeteria.services.MealsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MealsServiceImpl implements MealsService {

    private final MenuRepository menuRepository;
    private final JWTService jwtService;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final MealRepository mealRepository;
    
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

        List<Menu> menusWithDateConflict = menuRepository.findAllByAvailableFromAfterAndAvailableFromBeforeOrAvailableToAfterAndAvailableToBeforeOrAvailableFromOrAvailableToOrAvailableFromBeforeAndAvailableToAfter(
                new Date(request.getAvailableFrom()), new Date(request.getAvailableTo()), new Date(request.getAvailableFrom()),
                new Date(request.getAvailableTo()), new Date(request.getAvailableFrom()),
                new Date(request.getAvailableTo()), new Date(request.getAvailableFrom()), new Date(request.getAvailableFrom())
        );

        if(!menusWithDateConflict.isEmpty()) {
            throw new MenuAlreadyExistsException("There is already a menu with the same starting date");
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

        List<Menu> menusWithDateConflict = menuRepository.findAllByAvailableFromAfterAndAvailableFromBeforeOrAvailableToAfterAndAvailableToBeforeOrAvailableFromOrAvailableToOrAvailableFromBeforeAndAvailableToAfter(
                menu.getAvailableFrom(), menu.getAvailableTo(), menu.getAvailableFrom(), menu.getAvailableTo(), menu.getAvailableFrom(),
                menu.getAvailableTo(), menu.getAvailableFrom(), menu.getAvailableFrom()
        );
        System.out.println(menusWithDateConflict.size());

        if(!menusWithDateConflict.isEmpty()) {
            if(menusWithDateConflict.size() > 1) {
                throw new MenuAlreadyExistsException("There is an existing menu overlapping with the provided availability start date");
            }
            else {
                if(!menusWithDateConflict.get(0).getId().equals(menu.getId())) {
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

        List<Meal> mealList = menuToDelete.getMeals();

        for(Meal meal : mealList)
            mealRepository.delete(meal);

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

        response.setMeals(getMeals(currentMenu.getMeals()));

        return response;
    }

    private List<MealResponse> getMeals(List<Meal> mealList) {
        List<MealResponse> responseList = new ArrayList<>();
        for (Meal meal : mealList)
            mealToMealResponseList(meal, responseList);
        return responseList;
    }

    private void mealToMealResponseList(Meal meal, List<MealResponse> responseList) {
        MealResponse response = new MealResponse();

        response.setMealId(meal.getId());
        response.setIdentification(meal.getIdentification());
        response.setDescription(meal.getDescription());
        response.setDayOfMeal(meal.getDateOfMeal().getTime());

        responseList.add(response);
    }
    //endregion

    public MenuResponse createMeal(CreateMealRequest request) {
        Optional<Menu> optionalMenu = menuRepository.findById(request.getMenuId());

        if(optionalMenu.isEmpty())
            throw new MenuNotFoundException("Menu not found with the provided id");

        if(request.getDateOfMeal() == null || request.getDateOfMeal().toString().isEmpty() || request.getDateOfMeal().toString().isBlank())
            throw new BadCreateMealRequestException("No date of meal data provided!");

        Menu menu = optionalMenu.get();

        Date dateOfMeal = new Date(request.getDateOfMeal() + 43200000L);

        if(menu.getSelectionStart().before(new Date()))
            throw new BadCreateMealRequestException("Menu selection has started, you can't modify the meals anymore!");

        if(dateOfMeal.before(menu.getAvailableFrom()) || dateOfMeal.after(menu.getAvailableTo()))
            throw new BadCreateMealRequestException("The date of the meal falls out of the range defined in the menu!");

        Meal meal = new Meal();
        List<Meal> mealList = menu.getMeals();

        meal.setMenu(menu);
        meal.setDescription(request.getDescription());
        meal.setIdentification(request.getIdentification());
        meal.setDateOfMeal(dateOfMeal);

        mealList.add(meal);

        menu.setMeals(mealList);

        Menu savedMenu = menuRepository.save(menu);
        mealRepository.save(meal);
        return menuToMenuResponseDTO(savedMenu);
    }

    public MenuResponse deleteMeal(DeleteMealRequest request) {
        Optional<Menu> optionalMenu = menuRepository.findById(request.getMenuId());

        if(optionalMenu.isEmpty())
            throw new MenuNotFoundException("No menu found with the provided id");

        Menu menu = optionalMenu.get();

        if(menu.getSelectionStart().before(new Date()))
            throw new BadDeleteMealRequestException("Menu selection has started, you can't modify the meals anymore!");

        try {
            mealRepository.deleteById(request.getMealId());
            menu.setMeals(mealRepository.findAllByMenu(menu));
            return menuToMenuResponseDTO(menuRepository.save(menu));
        }
        catch (Exception exception) {
            throw new BadDeleteMealRequestException("Meal not found with the provided id: " + request.getMealId());
        }
    }

    private MenuResponse menuToMenuResponseDTO(Menu menu) {
        MenuResponse response = new MenuResponse();

        response.setMenuId(menu.getId());
        response.setSelectionStart(menu.getSelectionStart().getTime());
        response.setSelectionEnd(menu.getSelectionEnd().getTime());
        response.setAvailableFrom(menu.getAvailableFrom().getTime());
        response.setAvailableTo(menu.getAvailableTo().getTime());
        response.setMeals(getMeals(menu.getMeals()));

        return  response;
    }

    public SimpleMealResponse getMealForToday(JwtRequest request) {
        SimpleMealResponse mealResponse = new SimpleMealResponse();

        String userEmail = jwtService.extractUserName(request.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        Optional<Order> userOrder = orderRepository.findByAvailableFromBeforeAndAvailableToAfterAndUser(
                new Date(),
                new Date(),
                user
        );

        if(userOrder.isPresent()) {
            Order orderForWeek = userOrder.get();

            List<Meal> mealList = orderForWeek.getMeals();

            if (!mealList.isEmpty()) {
                for (Meal meal : mealList) {
                    if (isOnSameDay(meal.getDateOfMeal())) {
                        mealResponse.setDescription(meal.getDescription());
                        mealResponse.setIdentification(meal.getIdentification());
                        mealResponse.setDayOfMeal(meal.getDateOfMeal().getTime());
                    }
                }
            }
        }
        return mealResponse;
    }

    private static boolean isOnSameDay(Date date) {
        LocalDate localDate1 = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = new Date().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
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

        order.setAvailableFrom(menu.getAvailableFrom());
        order.setAvailableTo(menu.getAvailableTo());

        order.setUser(user);

        List<Meal> mealsToSave = new ArrayList<>();

        for(int i = 0; i < request.getMealIdList().size(); i++) {
            if(request.getMealIdList().get(i) != null)
                mealsToSave.add(mealRepository.findById(request.getMealIdList().get(i)).orElseThrow(() ->
                        new MealNotFoundException("No meal found with the provided id")));
        }

        order.setMeals(mealsToSave);

        orderRepository.save(order);
    }

    public OrdersResponse getOrdersFromMenu(Integer menuId) {
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);

        if(optionalMenu.isEmpty())
            throw new MenuNotFoundException("Menu not found with the provided id");

        Menu menu = optionalMenu.get();
        OrdersResponse response = new OrdersResponse();

        List<Order> orderList = orderRepository.findAllByAvailableFrom(menu.getAvailableFrom());
        List<Meal> mealList = new ArrayList<>();

        for (Order order : orderList) {
            mealList.addAll(order.getMeals());
        }

        mealList.sort(new MealComparator());

        List<MealOrderInformationResponse> orderInformationList = new ArrayList<>();
        Map<Meal, Integer> mealIntegerMap = new HashMap<>();
        
        for(Meal meal : mealList) {
            if(!mealIntegerMap.containsKey(meal)) {
                int numberOfOrders = 0;
                for (Meal mealInList : mealList) {
                    if (meal.equals(mealInList)) {
                        numberOfOrders = numberOfOrders + 1;
                    }
                }
                mealIntegerMap.put(meal, numberOfOrders);
            }
        }

        for(Meal meal : mealIntegerMap.keySet()) {
            MealOrderInformationResponse orderInformation = new MealOrderInformationResponse();
            orderInformation.setMeal(mealToSimpleMealResponseDTO(meal));
            orderInformation.setNumberOfOrders(mealIntegerMap.get(meal));
            orderInformationList.add(orderInformation);
        }

        response.setOrderedMeals(orderInformationList);

        return response;
    }

    private SimpleMealResponse mealToSimpleMealResponseDTO(Meal meal) {
        SimpleMealResponse response = new SimpleMealResponse();

        response.setDescription(meal.getDescription());
        response.setIdentification(meal.getIdentification());
        response.setDayOfMeal(meal.getDateOfMeal().getTime());

        return response;
    }
}