package com.projekt_pai.budzet.controllers;


import com.projekt_pai.budzet.entities.Category;
import com.projekt_pai.budzet.entities.Finance;
import com.projekt_pai.budzet.entities.User;
import com.projekt_pai.budzet.repositories.CategoryRepository;
import com.projekt_pai.budzet.repositories.FinanceRepository;
import com.projekt_pai.budzet.repositories.UserRepository;
import com.sun.el.stream.Stream;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Controller
public class StatisticsController {

    private final UserRepository userRepository;
    private final FinanceRepository financeRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public StatisticsController(UserRepository userRepository, FinanceRepository financeRepository, CategoryRepository categoryRepository){
        this.userRepository=userRepository;
        this.financeRepository=financeRepository;
        this.categoryRepository=categoryRepository;
    }

    @GetMapping("/statistics")
    public String getStatisticsPage(@CookieValue(value = "username", required = false)String username,
                                    Model model){
        User user = userRepository.findByEmail(username);
        if(username==null || username.isEmpty()||user==null){
            return "redirect:/login";
        }

        Map<String , Integer> incAmountsPerCategory = countAmountForCategory(financeRepository.findAllByUserIdAndType(user.getId(),"income"));
        Map<String , Integer> expAmountsPerCategory = countAmountForCategory(financeRepository.findAllByUserIdAndType(user.getId(),"expense"));
        model.addAttribute("incAmountsPerCategory", incAmountsPerCategory);
        model.addAttribute("expAmountsPerCategory", expAmountsPerCategory);

        return "statistics";
    }

    public Map<String , Integer> countAmountForCategory(List<Finance> financeList){
        return financeList.stream()
                .collect(groupingBy(o -> o.getcategoryId().toString().replace(o.getcategoryId().toString(),
                        findCategoryNameFromId(o.getcategoryId())), summingInt(Finance::getAmount)));
    }




    public String findCategoryNameFromId(Integer id){
        Optional<Category> category = categoryRepository.findById(id);
        return category.get().getName();
    }
}
