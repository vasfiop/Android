# 这个是2022年大学生计算机设计大赛的比赛项目

## 项目名称:NewGame

## 项目成员:

## 项目目标:尽量复刻京东的各种布局

### 项目由2021年大作业更改

#### 原项目地址 [https://github.com/vasfiop/Android_2021_6_5](https://github.com/vasfiop/Android_2021_6_5)

1. 项目规则
    >内部的各种drawable起名全部按照当前Activity名+fragment名+具体功能名  
    >java代码也对fragment层，Activity层，工具层进行分包管理  
    >layout代码命名规则按照实现java层名称+当前布局名字+具体功能名  
2. 使用环境和添加依赖
    > okhttp3:okhttp-bom:4.9.3  
    > 阿里巴巴fastjson  
    > android api 31  

3. 工程视图  
    - activity
        > Login_Activity  
        > MainActivity  
    - fragment
        >
        > - find  
        >
            >> first_find_fragment  
            >> second_find_fragment  
        >
        > - homepage  
        >  
            >> Buildhomepage_fragment
            >> Firsthomepage_fragment
            >> Officehomepage_fragment
            >> Phonehomepage_fragment  
        >
        > - sort  
        > Car_fragment  
        > Find_fragment  
        > Homepage_fragment  
        > Me_fragment  
        > Sort_fragment  
        >
    - myView  
        > MyImageView  
4. 具体实现的细节功能（特色）  
    - 集成化字符串  
        - 所有的字符串参数全部在`<string>`文件中定义
    - 国际化字符串
        - 初步实现了对默认和en-rUS字符串的定义  
    - 颜色的主题
        - 通过使用ps取色板等各种主题颜色的RGB
    - `<style>`小框架
        - 通过提前定义中等规模的`<style>`来实现布局文件中的各种控件的样式调用
    - 布局文件的编写经过了`<include>`的模块化
    - 各种类型的圆角，定义了超过8大种圆角类型，方便各种布局文件的反复调用
    - 两个自定义的anim
    - 10+个selector
    - 自定义小型数据库，实现用户登陆的验证  
    - `git`+`github`的管理模式
        - `github`：[https://github.com/vasfiop/Android](https://github.com/vasfiop/Android)
