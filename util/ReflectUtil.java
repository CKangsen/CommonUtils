package com.threetree.baseproject.util;

import java.lang.reflect.ParameterizedType;

/**
 * 说明: 泛型工具类 <br/>
 * 邮箱: yangbin5052@gmail.com <br/>
 * Create at 2017/12/22-上午9:20 by Luuren
 */
public class ReflectUtil {

  /**
   * 如果转换报错,解决如下:T1父类不用修改代码，T2中继承父类的时候指定父类的类型就可以了。
   * http://blog.csdn.net/q1054261752/article/details/54946280
   *
   * @param o object i index
   * @return 具体类型
   * @author caiyangbin
   * create at 2017/6/18 18:42
   */
  public static <T> T getT(Object o, int i) {
    try {
      return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (ClassCastException e) {
      e.printStackTrace();
    }
    return null;
  }
}