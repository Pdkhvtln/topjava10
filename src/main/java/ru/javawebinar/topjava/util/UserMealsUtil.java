package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),//!!
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),//!!
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );


        for (UserMealWithExceed item :
                getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)) {
            System.out.println(item.toString());
        }
        // getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    /*https://habrahabr.ru/company/luxoft/blog/270383/
    https://www.mkyong.com/java8/java-8-collectors-groupingby-and-mapping-example/
    http://javadevblog.com/polnoe-rukovodstvo-po-java-8-stream.html
    */
/*https://github.com/JavaOPs/topjava#-%D0%94%D0%BE%D0%BC%D0%B0%D1%88%D0%BD%D0%B5%D0%B5-%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-hw0
Реализовать метод UserMealsUtil.getFilteredWithExceeded:
-  должны возвращаться только записи между startTime и endTime
-  поле UserMealWithExceed.exceed должно показывать,
                                     превышает ли сумма калорий за весь день параметра метода caloriesPerDay

Т.е UserMealWithExceed - это запись одной еды, но поле exceeded будет одинаково для всех записей за этот день.

- Проверте результат выполнения ДЗ (можно проверить логику в http://topjava.herokuapp.com , список еды)
- Оцените Time complexity вашего алгоритма, если он O(N*N)- попробуйте сделать O(N).*/
    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        List<UserMealWithExceed> result = new ArrayList<>();
        List<UserMeal> mealOfDay = new ArrayList<>();
        LocalDate oldLD = null;
        int i = 0;
        for (UserMeal item : mealList) {
            i++;
            if (oldLD == null)
                oldLD = item.getDateTime().toLocalDate();
            if (!oldLD.isEqual(item.getDateTime().toLocalDate()) || i == mealList.size()) {//
                //The last element of the list meal
                if (oldLD.isEqual(item.getDateTime().toLocalDate()) && i == mealList.size())
                    mealOfDay.add(item);
                oldLD = item.getDateTime().toLocalDate();
                //solving sum calories for the day
                int sumMeal = 0;
                for (UserMeal itemDay : mealOfDay) {
                    sumMeal += itemDay.getCalories();
                }

                boolean exceed = (sumMeal > caloriesPerDay);
                for (UserMeal itemDay : mealOfDay) {
                    LocalTime lt = itemDay.getDateTime().toLocalTime();
                    if (startTime.isBefore(lt) && endTime.isAfter(lt)) {
                        UserMealWithExceed userMealWithExceed = new UserMealWithExceed(itemDay.getDateTime(), itemDay.getDescription(), itemDay.getCalories(), exceed);
                        result.add(userMealWithExceed);
                    }
                }
                //a new day
                mealOfDay = new ArrayList<>();
            }
            mealOfDay.add(item);
        }
        return result;
    }
}
