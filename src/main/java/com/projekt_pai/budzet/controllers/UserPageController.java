package com.projekt_pai.budzet.controllers;

import com.projekt_pai.budzet.Additional.Filters;
import com.projekt_pai.budzet.entities.Category;
import com.projekt_pai.budzet.entities.Finance;
import com.projekt_pai.budzet.entities.User;
import com.projekt_pai.budzet.repositories.CategoryRepository;
import com.projekt_pai.budzet.repositories.FinanceRepository;
import com.projekt_pai.budzet.repositories.UserRepository;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserPageController {
    private final UserRepository userRepository;
    private final FinanceRepository financeRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public UserPageController(UserRepository userRepository, FinanceRepository financeRepository, CategoryRepository categoryRepository){
        this.userRepository=userRepository;
        this.financeRepository=financeRepository;
        this.categoryRepository=categoryRepository;
    }


    @GetMapping("/user_page")
    public String get(@CookieValue(value = "username", required = false)String username,
                      @RequestParam(value = "action", defaultValue = "finances") String action,
                      @ModelAttribute("filters") Filters filters,
                      Model model,
                      HttpServletResponse response){

/*
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "dateFrom", required = false) String dateFrom,
        @RequestParam(value = "dateTo", required = false) String dateTo,
        @RequestParam(value = "categoryId", required = false) Integer categoryId,
        @RequestParam(value = "type",required = false) String type,*/

        User user = userRepository.findByEmail(username);
        if(username==null || username.isEmpty()||user==null||action==null){
            return "redirect:/login";
        }


        switch (action) {
            case "account_info":
                model.addAttribute("user", user);
                break;
            case "finances":
                List<Finance> finances = financeRepository.findAllByUserId(user.getId());
                List<Category> allCategories = (List<Category>) categoryRepository.findAll();

                if(filters.getKeyword()!=null) {
                    finances = filterFinances(finances, filters);
                }
                if(filters==null) {
                    filters = new Filters();
                }
                model.addAttribute("finances", finances);
                model.addAttribute("allCategories", allCategories);
                model.addAttribute("filters",filters);

                break;
            case "add_income": {
                List<Category> incomeCategories = categoryRepository.findByType("income");
                model.addAttribute("incomeCategories", incomeCategories);
                model.addAttribute("finance", new Finance());
                break;
            }
            case "add_expense": {
                List<Category> expenseCategories = categoryRepository.findByType("expense");
                model.addAttribute("expenseCategories", expenseCategories);
                model.addAttribute("finance", new Finance());
                break;
            }
            case "logout":
                //delete cookie
                Cookie cookie = new Cookie("username", null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                return "redirect:/login";

        }

        model.addAttribute("action", action);
        return "user_page";
    }

    public List<Finance> filterFinances(List<Finance> finances, Filters filters){
        List<Finance> result = new ArrayList<>();

        Observable.fromIterable(finances)
                .flatMap(finance -> {
                    if(!(filters.getKeyword().isEmpty()) && filters.getKeyword()!=null)
                        return Observable.just(finance)
                                .filter((financeRepository.findAllByNameContains(filters.getKeyword()))::contains);
                    else
                        return Observable.just(finance);
                })
                .flatMap(finance -> {
                    if(!(filters.getType().isEmpty()))
                        return Observable.just(finance)
                                .filter((financeRepository.findAllByType(filters.getType()))::contains);
                    else
                        return Observable.just(finance);
                })
                .flatMap(finance -> {
                    if(!(filters.getFromDate().isEmpty()))
                        return Observable.just(finance)
                                .filter((financeRepository.findAllByDateAfter(filters.getFromDate()))::contains);
                    else
                        return Observable.just(finance);

                })
                .flatMap(finance -> {
                    if(!(filters.getToDate().isEmpty()))
                        return Observable.just(finance)
                                .filter((financeRepository.findAllByDateBefore(filters.getToDate()))::contains);
                    else
                        return Observable.just(finance);

                })
                .flatMap(finance -> {
                    if(!(filters.getCategoryId()==null))
                        return Observable.just(finance)
                                .filter((financeRepository.findAllByCategoryId(filters.getCategoryId()))::contains);
                    else
                        return Observable.just(finance);

                })
                .doOnError(error -> System.err.println("The error message is: " + error.getMessage()))
                .subscribe(result::add,
                        Throwable::printStackTrace);

        return result;
    }


    @GetMapping("/delete_finance")
    public String deleteItem(@RequestParam(value="financeId", required = true)Integer financeId,
                             RedirectAttributes redirectAttributes){

        Finance finance=financeRepository.findFinanceById(financeId);
        financeRepository.delete(finance);

        redirectAttributes.addAttribute("action","finances");
        return "redirect:/user_page";
    }

    public Integer getUserIdFromUsername(String username){
        User user = userRepository.findByEmail(username);
        return user.getId();
    }

    @PostMapping("/add")
    public String addFinance(@ModelAttribute Finance finance,
                             @CookieValue(value = "username") String username,
                             Model model,
                             RedirectAttributes redirectAttributes
    ){
        finance.setuserId(getUserIdFromUsername(username));

        if(finance.getName().isEmpty()){
            System.out.println("getName is empty");
        }else if(finance.getType().isEmpty()){
            System.out.println("getType is empty");
        }
        else if(finance.getAmount()==null){
            System.out.println("getAmount is empty");
        }
        else if(finance.getDate()==null){
            System.out.println("getDate is empty");
        }
        else if(finance.getcategoryId()==null){
            System.out.println("getcategoryId is empty" + finance.getcategoryId());
        }
        else {
            financeRepository.save(finance);

            System.out.println("finance added");
        }

        redirectAttributes.addAttribute("action","finances");
        return "redirect:/user_page";

    }

    @PostMapping("/filter")
    public String filterFinances(@ModelAttribute Filters filters,
                                 RedirectAttributes redirectAttributes){

        redirectAttributes.addAttribute("action","finances");
        redirectAttributes.addFlashAttribute("filters",filters);

        return "redirect:/user_page";
    }

}
