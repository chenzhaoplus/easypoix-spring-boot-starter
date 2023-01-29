# 自我介绍

>IT 果果
>一个普通的技术宅，欢迎关注、点赞，请多关照。
# 入门和安装

## 简介

为了满足项目中快速实现 excel 导入导出的功能，将 excel 开源工具 easypoi 整合成 spring-boot-starter 的方式，在做到对 easypoi 无侵入的同时减少了一些重复代码。例如:

* 增强了 easypoi 对于字段值重复校验的功能；
* 提升了字典转换的便捷性和效率；
* 增加了字段之间联动转换的功能；
  这些场景也是我在平时项目上经常会用到的功能点，为了避免重复造轮子，减少冗余代码，所以写了一个 easypoix-spring-boot-starter 扩展 jar 包。

## 安装

* 在你的 maven 项目中引用 maven 依赖
```xml
<dependency>
    <groupId>com.itguoguo</groupId>
    <artifactId>easypoix-spring-boot-starter</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```

* 在你的 spring boot 项目配置文件中加入字典前缀（可选）
```plain
easypoix.dictPrefix=basic:dict:
```

# 工具

## spring-boot-starter

**SpringBoot 核心就是几个注解：SpringBootConfiguration、EnableAutoConfiguration、ComponentScan**，依赖这几个注解完成了所谓自动装配的功能，这个自动装配说简单点就是把你需要的 Bean 注入到 Spring 容器里面。（SpringBootApplication 启动类上的注解，只是简单的组合了 SpringBootConfiguration、EnableAutoConfiguration、ComponentScan 几个注解，避免开发者一个一个的去加）。

**SpringBoot 程序在启动过程中会解析 SpringBootConfiguration、EnableAutoConfiguration、ComponentScan 三个注解**：

* SpringBootConfiguration：包含了 Configuration 注解，实现配置文件
* ComponentScan：指定扫描范围
* EnableAutoConfiguration：通过源码可以知道，该注解使用 Import 引入了 AutoConfigurationImportSelector 类，而 AutoConfigurationImportSelector 类通过 SpringFactortisLoader 加载了所有 jar 包的 MATE-INF 文件夹下面的 spring.factories 文件，spring.factories 包含了所有需要装配的 XXXConfiguration 类的全限定名。XXXConfiguration 类包含了实例化该类需要的信息，比如说如果这是个数据源 Configuration 类，那么就应该有数据库驱动、用户名、密码等等信息。
  **Spring Boot 在启动的时候会干这几件事情**：  

* Spring Boot 在启动时会去依赖的 Starter 包中寻找 resources/META-INF/spring.factories 文件，然后根据文件中配置的 Jar 包去扫描项目所依赖的 Jar 包。 
* 根据 spring.factories 配置加载 AutoConfigure 类  
* 根据 @Conditional 注解的条件，进行自动配置并将 Bean 注入 Spring Context
                                                                                                                                                                                                                                                **总结**：

>SpringBoot 并没有想象那么神秘，就是这么几件事情：
>1.提供了一个配置类，该配置类定义了我们需要的对象的实例化过程；
>2.提供了一个 spring.factories 文件，包含了配置类的全限定名；
>3.将配置类和 spring.factories 文件打包为一个启动器 starter；
>4.程序启动时通过加载 starter.jar 包的 spring.factories 文件信息，然后通过反射实例化文件里面的类。
## easypoi

**独特的功能**

* 基于注解的导入导出,修改注解就可以修改 Excel
* 支持常用的样式自定义
* 基于 map 可以灵活定义的表头字段
* 支持一对多的导出,导入
* 支持模板的导出,一些常见的标签,自定义标签
* 支持 HTML/Excel 转换,如果模板还不能满足用户的变态需求,请用这个功能
* 支持 word 的导出,支持图片,Excel
  **使用**

* 1.easypoi 父包–作用大家都懂得
* 2.easypoi-annotation 基础注解包,作用于实体对象上,拆分后方便 maven 多工程的依赖管理
* 3.easypoi-base 导入导出的工具包,可以完成 Excel 导出,导入,Word 的导出,Excel 的导出功能
* 4.easypoi-web 耦合了 spring-mvc 基于 AbstractView,极大的简化 spring-mvc 下的导出功能
* 5.sax 导入使用 xercesImpl 这个包(这个包可能造成奇怪的问题哈),word 导出使用 poi-scratchpad,都作为可选包了
  **Maven 坐标**

```xml
<dependency>
    <groupId>cn.afterturn</groupId>
    <artifactId>easypoi-base</artifactId>
    <version>4.1.0</version>
</dependency>
<dependency>
    <groupId>cn.afterturn</groupId>
    <artifactId>easypoi-web</artifactId>
    <version>4.1.0</version>
</dependency>
<dependency>
    <groupId>cn.afterturn</groupId>
    <artifactId>easypoi-annotation</artifactId>
    <version>4.1.0</version>
</dependency>
```

# 使用场景

我们的目标是尽可能使用一个工具类方法就能实现简单的导出和导入 Excel 的功能，以下列出了几个常见的场景供参考。

## 简单导出 Excel

### 实现步骤

新建一个导出实体模型，@ExcelFileAttr 注解提供导出 Excel 的文件名，@Excel 注解提供表头的字段，其中 name 属性是字段名称，dict 属性是字典的关键字常量

```java
@Data
@ExcelFileAttr(fileName = "导出.xls")
public class ExportPersonExcelModal implements Serializable {
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "性别", dict = DICT_XB)
    private String sex;
    @Excel(name = "民族", dict = DICT_MZ)
    private String mz;
}
```

使用工具类 EasyPoiUtil 的 exportExcel 方法即可实现 excel 导出功能，只需要传递两个参数 。

* Excel 数据列表
* HttpServletResponse 输出流
```java
EasyPoiUtil.exportExcel(list, response);
```

### **比较 EasyPoi 和 EasyPoiX**

相对于 EasyPoi 官方的导出方式，EasyPoiX 对于导出时字典的处理更加方便，不需要额外提供

IExcelDictHandler 实现，EasyPoi 的 IExcelDictHandler 接口两个实现方法如下：

```java
/**
 * 从值翻译到名称
 * @param dict  字典 Key
 * @param obj   对象
 * @param name  属性名称
 * @param value 属性值
 * @return
 */
public String toName(String dict, Object obj, String name, Object value);

/**
 * 从名称翻译到值
 * @param dict  字典 Key
 * @param obj   对象
 * @param name  属性名称
 * @param value 属性值
 * @return
 */
public String toValue(String dict, Object obj, String name, Object value);
```

toName 方法用于导出时字典值到字典名称的转换；toValue 方法用于导入时字典名称到字典值的转换。如果使用 EasyPoi，则多个字典公用一个 IExcelDictHandler 接口时需要多个 if-else 判断，并且在 Excel 导入导出时重复查询同一个字典时会有效率问题，另外在项目中多个字典的查询接口往往是同一个方法，所以显得代码重复不美观。

为了解决以上问题，EasyPoiX 在导出时提供了字典转换的默认字典转换接口，且不需要在工具类中传参指定（如果有特殊要求也可以指定自定义的字典转换接口）。那么问题来了，字典的取值方法在哪里定义呢？EasyPoiX 提供了一个@ExcelDictDataType 注解和一个 ExcelDictDataService 接口，代码示例如下：

```java
@ExcelDictDataType({DICT_XB, DICT_MZ, DICT_COMMUNITY})
@Component
public class BasicDictDataService implements ExcelDictDataService<String, String> {
    public static final String DICT_XB = "XB";
    public static final String DICT_MZ = "MZ";

    @Override
    public Map<String, String> getData(DataParam params) {
        // 获取字典值的实现代码
        // 不论是字典的名称转值还是值转名称，都会从这里取值
        // 返回的 Map 类型，key 是字典值，value 是字典名称
        return map;
    }
}
```

* IExcelDictHandler 接口，要在导出方法里显示的传参；ExcelDictDataService 接口不需要显示传参，因为@ExcelDictDataType 注解已经标注当前类就是字典取值的实现类，注解的 value()属性里配置的是字典的 key，可以提供多个字典的 key，所以 ExcelDictDataService 接口可以满足多个字典的取值。
* IExcelDictHandler 接口需要正反向两个转换方法；ExcelDictDataService 接口只需要实现一个字典取值方法。
* IExcelDictHandler 接口需要在实现方法中加入多个 if-else，并且在 Excel 导入导出时频繁调用 toName 和 toValue 方法会导致效率问题；ExcelDictDataService 接口不需要通过 if-else 判断，且每次取值都会先从本地缓存中查询，如果缓存没有才会进行真正的查询调用。在一次请求结束之后，会清除这些缓存以免造成内存不足。
## 大数据导出 Excel

### 实现步骤

使用工具类 EasyPoiUtil 的 exportBigExcel 方法即可实现 excel 大数据导出功能，传参：

* 实体模型 Class
* IExcelExportServer 接口。可以使用默认接口实现类 DefaultBigExcelExportServer，构造方法需要提供一个分页查询的 lamda 表达式
* 分页查询的条件参数，Object 类型
* HttpServletResponse 输出流
```java
DefaultBigExcelExportServer ser = new DefaultBigExcelExportServer((queryParams, page) -> findPage(queryParams, page));
EasyPoiUtil.exportBigExcel(ExportPersonExcelModal.class, ser, param, response);
```
### **比较 EasyPoi 和 EasyPoiX**

EasyPoi 需要实现 IExcelExportServer 接口，EasyPoiX 不需要实现，而是传递一个 DefaultBigExcelExportServer 对象，构造对象时提供一个分页查询的 lamda 表达式

或者叫分页查询方法。这样的好处是不需要为每一个 Excel 都单独新建一个 IExcelExportServer 接口实现类。

```java
/**
 * 导出数据接口
 */
public interface IExcelExportServer {
    /**
     * 查询数据接口
     * @param queryParams 查询条件
     * @param page        当前页数从 1 开始
     * @return
     */
    public List<Object> selectListForExcelExport(Object queryParams, int page);
}
```

## 导入 Excel

导入 Excel 场景相对于导出场景要复杂一些，除了调用工具类的导入方法和字典转换取值实现以外，还额外增加了两个场景 ：1、数据校验；2、数据处理。这两个场景都不是导入时的必选步骤，但都很常见。下面就对这两个场景详细说明一下。

### 数据校验

EasyPoi 提供了一个数据校验接口 IExcelVerifyHandler

```java
/**
 * 导入校验接口
 * @author JueYue
 *  2014 年 6 月 23 日 下午 11:08:21
 */
public interface IExcelVerifyHandler<T> {
    /**
     * 导入校验方法
     * @param obj
     *            当前对象
     * @return
     */
    public ExcelVerifyHandlerResult verifyHandler(T obj);
}
```

EasyPoiX 对其进行了一个小优化，增加了数据重复的校验功能，比如导入人员名单时，出现了相同的身份证号会提示后面出现的那个身份证号重复。使用方法也很简单，只需要继承一个 DefaultExcelVerifyHandler 类即可，示例代码如下：

```java
@Component
public class ImportPersonExcelVerifyHandler extends DefaultExcelVerifyHandler<ImportPersonExcelModal> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportPersonExcelModal obj) {
        Set<Object> idCards = getRepeatTmp("idCard");
        if (idCards.contains(obj.getIdCard())) {
            return new ExcelVerifyHandlerResult(false, "证件号重复");
        }
        idCards.add(obj.getIdCard());
        return new ExcelVerifyHandlerResult(true);
    }
}
```

这里要特别指出一下，返回的 ExcelVerifyHandlerResult 类型构造时如果 success 参数传 true 就表示校验通过；如果传 false，就会在导入方法返回的导入数据中校验不通过的那个元素中，errorMsg 设置为校验错误的提示信息。所以导入的实体类需要继承 ImportExcelModel 类。

```java
public class ImportExcelModel implements Serializable, IExcelDataModel, IExcelModel {
    /**
     * 行号
     */
    private int rowNum;
    /**
     * 错误消息
     */
    @Excel(name = "错误信息")
    private String errorMsg;

    @Override
    public int getRowNum() {
        return rowNum;
    }
    @Override
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
```

### 数据处理

**数据处理的场景其实和数据字典的场景非常相似**，都是需要对 excel 数据做一些转换操作。但是他们的**不同点**在于数据字典更偏向于转换全局唯一的常量，所以@Excel 注解里的 dict 属性在不同 Excel 实体里都是共用的；而数据处理是在数据字典转换操作完成以后进行的，因为数据处理的字段在转换时可能会用到当前 Excel 行的其他字段，包括转换后的字典值。

举个例子，比如导入的 excel 行数据多个字段之间存在联动查询的关系，就需要自定义数据处理了。例如导入的一行字段里有小区和楼栋，小区名称和小区 id 的转换不需要依赖其他字段，但是楼栋名称和楼栋 id 的转换是需要根据小区 id 查询的，虽然同样可以通过字典方式转换，但是最好还是在字典转换之后，数据处理的步骤再转换更安全。

此外，字典转换做不到同一个关键字在不同实体里，使用不同的转换逻辑，因为字典的含义本身就是全局唯一的。而数据处理转换可以做到同一个关键字在不同实体里，使用不同的转换实现。下面用 EasyPoi 和 EasyPoiX 的两种代码实现来更加详细的介绍他们的区别。

#### **EasyPoi 的数据处理**

EasyPoi 提供了默认数据处理器虚类 ExcelDataHandlerDefaultImpl

```java
public abstract class ExcelDataHandlerDefaultImpl<T> implements IExcelDataHandler<T> {
    /**
     * 需要处理的字段
     */
    private String[] needHandlerFields;

    // 省略部分代码.....

    @Override
    public Object importHandler(T obj, String name, Object value) {
        return value;
    }
    @Override
    public Object exportHandler(T obj, String name, Object value) {
        return value;
    }
    @Override
    public void setNeedHandlerFields(String[] needHandlerFields) {
        this.needHandlerFields = needHandlerFields;
    }
}
```
* setNeedHandlerFields 方法用来指定哪些字段需要处理，参数是一个字符串数组，每个元素对应的是 Excel 实体里@Excel 注解的 name 属性，即 Excel 的表头名称
* importHandler 接口可以重写为我们需要的转换逻辑，默认是返回原始 value，即不转换字段
* exportHandler 接口可以重写为我们需要的转换逻辑，默认是返回原始 value，即不转换字段
  现在，继承 ExcelDataHandlerDefaultImpl 类就可以实现数据处理了

```java
@Component
public class PersonExcelDataHandler extends ExcelDataHandlerDefaultImpl<ImportPersonExcelModal> {
    @PostConstruct
    public void init() {
        setNeedHandlerFields(new String[]{"小区", "楼栋"});
    }
    @Override
    public Object importHandler(ImportPersonExcelModal obj, String name, Object value) {
        if(Objects.isNull(value)){
            return value;
        }
        if(name.equals("楼栋")){
            //根据 obj.getCommunityId() 和 obj.getBuildingName()，查询楼栋 id
			return 楼栋 id;
        }
        return value;
    }
    @Override
    public Object exportHandler(ImportPersonExcelModal obj, String name, Object value) {
        if(Objects.isNull(value)){
            return value;
        }
        if(name.equals("楼栋")){
            //根据 obj.getCommunityId() 和 obj.getBuildingId()，查询楼栋名称
			return 楼栋名称;
        }
        return value;
    }
}
```

#### **EasyPoiX 的数据处理**

EasyPoiX 封装了一个 SimpleExcelDataHandler 虚类，继承这个虚类就可以免去手动调用 setNeedHandlerFields 方法的麻烦。

另外，SimpleExcelDataHandler 默认提供了 importHandler 和 exportHandler 两个接口的实现，同字典转换一样，只需要提供一个 getData 接口的实现，提供待处理数据的取值逻辑即可。

```java
@Component
@ExcelHandleDataType(clazz = ImportPersonExcelModal.class, value = {BUILDING_NAME, UNIT_NAME})
public class PersonExcelDataHandler extends SimpleExcelDataHandler<ImportPersonExcelModal> {
    public static final String BUILDING_NAME = "楼栋";
    public static final String UNIT_NAME = "单元";
    
    @Override
    public Map<String, String> getData(DataParam params) {
        switch (params.getDict()) {
            case BUILDING_NAME:
                return getBuildingData(params);
            case UNIT_NAME:
                return getUnitData(params);
            default:
                return null;
        }
    }

    private Map<String, String> getBuildingData(DataParam params) {
        Object row = params.getRow();
        String communityId = BeanUtil.getProperty(row, "communityId");
        //返回楼栋键值对，key 是楼栋 id，value 是楼栋名称
    }

    private Map<String, String> getUnitData(DataParam params) {
        Object row = params.getRow();
        String buildingId = BeanUtil.getProperty(row, "buildingId");
        //返回单元键值对，key 是单元 id，value 是单元名称
    }

}
```

DataParam.java

```java
@Data
@Accessors(chain = true)
public class DataParam {
    private String dict;
    private String key;
    private Object row;
}
```

如果实际业务不需要默认的数据处理实现，也可以在继承 SimpleExcelDataHandler 虚类之后，重写 importHandler 和 exportHandler 接口，就像 EasyPoi 的数据处理方式一样，这样更加灵活。

## 总结

EasyPoiX 是为了能够快速导入导出 Excel，而对 EasyPoi 进行的一个无侵入的二次封装，同时利用了 spring-boot-starter 的原理，使其在 spring-boot 项目中可以以 jar 模块的形式复用 EasyPoiX 的功能。如果有小伙伴在实际的使用中遇到了其他场景，也可以联系我。

我叫 **IT果果**，下面有我的联系方式！

# 请联系我

以上场景都有对应的测试代码示例，如果想更进一步了解请访问地址：

[https://gitee.com/chenzhaoplus/easypoix-spring-boot-starter](https://gitee.com/chenzhaoplus/easypoix-spring-boot-starter.git)

[https://github.com/chenzhaoplus/easypoix-spring-boot-starter](https://github.com/chenzhaoplus/easypoix-spring-boot-starter.git)

[https://blog.csdn.net/cz285933169?spm=1010.2135.3001.5421](https://blog.csdn.net/cz285933169?spm=1010.2135.3001.5421)





