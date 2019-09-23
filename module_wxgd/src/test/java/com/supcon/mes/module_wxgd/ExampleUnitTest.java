package com.supcon.mes.module_wxgd;

import android.text.format.DateUtils;

import com.supcon.mes.middleware.constant.Module;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {

        try {
        People people;
        for (int i = 0; i < 10; i++) {

            people = new People(10,i+ "");
            if (i == 5){
                people = null;
            }
                System.out.println(people.equals(0));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        List<Object> listt = new ArrayList<>();
        listt.add(null);
        listt.add(1);


        StringBuilder sb = new StringBuilder();
        System.out.println(sb.toString());

        BigDecimal big = new BigDecimal("2.3113"); //设置BigDecimal初始值
//        big.setScale(1);  //2.4 保留1位小数，默认用四舍五入。
//        System.out.println(big.setScale(1, BigDecimal.ROUND_UP));  //2.3 直接删除多余的小数，2.3513直接被截断位2.3
//        System.out.println(big.setScale(1, BigDecimal.ROUND_HALF_UP));  //2.4 四舍五入，向上舍入，2.3513变成2.4
//        System.out.println(big.setScale(1, BigDecimal.ROUND_HALF_DOWN));  //四舍五入，向下舍入，2.3513变成2。3


        double d = 2.246;
//        d --;
        d = new BigDecimal(String.valueOf(d)).subtract(new BigDecimal(1)).doubleValue();
        System.out.println(d);

        double a = 100.56;
        for (int i = 0; i < 10; i++) {
            a++;
            System.out.println(a);
        }

//        Integer.valueOf("9999999999");
        System.out.println(Integer.valueOf("999999999"));

        String name = null;
        Optional<String> opt = Optional.ofNullable(name);
        System.out.println(opt.isPresent());


        List<String> list = new ArrayList<>();
        list.add("san");
        list.add("si");

        String[] array = new String[]{};
        array = list.toArray(array);
        for (String e : array) {
            System.out.println(e);
        }

        String[] str = new String[]{"one", "two"};
        List<String> listt1 = Arrays.asList(str);
        str[0] = "one_one";
        String item = listt1.get(0);

        /*List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        for (String item : list) {
            if ("2".equals(item)) {
                list.remove(item);
                break;
            }
        }*/

        Map<String, String> map = new HashMap<>(2);
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");
        map.put("4", "four");

        map.keySet().forEach((s) -> {
            System.out.println(s + " : ");
        });

        Executors.newFixedThreadPool(800);

//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();
        System.out.println("----------------------------------------------------");
//        System.out.println(new Date());
//        System.out.println(Instant.now());
//        System.out.println(LocalDate.now());
//
//        LocalDateTime localDateTime = LocalDateTime.now();
//        System.out.println(localDateTime);
//
//        LocalDateTime localDateTime1 = LocalDateTime.of(2018,1,3,01,30);
//        System.out.println(localDateTime1.getMonthValue());
        System.out.println(Module.Fault.getName());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(simpleDateFormat.format(new Date()));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        now.plusHours(1);
        System.out.println(now.format(dateTimeFormatter));

        Random random = new Random(10);
        System.out.println(random.nextInt());


        SimpleDateFormat df = new SimpleDateFormat(DateUtils.YEAR_FORMAT);
        System.out.println(df.format(new Date()));


    }
}