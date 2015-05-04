# testrail-unit
## -- English --
## outline(function)
<dl>
  <dt>confirm junit test and testrail.</dt>
  <dd>If you run junit test with this tool, this tool reflect to testrail as the following rule.</dd>
</dl>
| junit            | testrail       |
|:-----------------|---------------:|
| test class name  | section name   |
| test method name | test case name |

<dl>
  <dt>reflect junit test result(assert result) to testrail.</dt>
</dl>

## how to use
### preparation
1. prepare testrail-unit.jar in your project.
2. prepare [testrail-unit.json](https://github.com/itoshige/testrail-unit/blob/develop/src/test/resources/testrail-unit.json) in your project.
3. create empty test case in testrail.
4. create empty test run from 3, test case.
5. modify testrail-unit.json.
```
{
    "url":"http://example.com/", # testrail api url

    "user":"hoge",               # testrail user

    "password":"hoge",           # testrail password

    "isDisabled":false,          # If you don't need to execute testrail-unit, please change true.

    "sync":true,                 # If you don't want to delete test case in testrail
                                 # (If you remove same junit test case, this tool also remove test case in testrail.)
                                 # please change false.

    "isDebugEnabled":false,      # If you want to output this tool's log, please change true.

    "runIds":[
        {
        "target":"com.github.itoshige.testrail", # input your project package name(junit test case exist)
        "runId":"359"                            # input your testrail runId(you created in 4.).
        },
    ]
}
```

### how to use
[1] set @ClassRule and @Rule in base junit test class.
[Please check it](https://github.com/itoshige/testrail-unit/blob/develop/src/test/java/com/github/itoshige/testrail/TestBase.java)
```
@ClassRule
public static TestRailUnit tr = new TestRailUnit();
@Rule
public TestRailStorage ts = new TestRailStorage(tr);
...
```
[2] If you don't want to reflect junit result to testrail, Please use @IgnoreTestRail
[Please check it](https://github.com/itoshige/testrail-unit/blob/develop/src/test/java/com/github/itoshige/testrail/rules/TestRailClassAddTest.java)
```
@IgnoreTestRail
@Test
public void Sample_01() {
...

* you can also use @IgnoreTestRail to class.
```

[3] If you want to change section name, Please use @Section
```
@Section(name = "サンプルセクション ")
public class TestRailClassAddTest extends TestBase {
...
```

## -- 日本語 --
## outline(機能説明)
<dl>
  <dt>導入するだけで、junitテストケースの内容をtestrailに反映してくれます</dt>
  <dd>※junitテストを作成すれば、testrailを作成する必要がなくなります</dd>
</dl>

<dl>
  <dt>junitテストケース結果についても、反映します</dt>
</dl>

## how to use
### preparation
1. testrail-unit.jar をプロジェクトに配置する
2. [testrail-unit.json](https://github.com/itoshige/testrail-unit/blob/develop/src/test/resources/testrail-unit.json) をプロジェクトに配置する
3. 空のtest caseを、testrailに作成する
4. 3.で作成したtest caseから、test runを作成する（空のままでよい）
5. 必要があれば、以下のように testrail-unit.json を修正する
```
{
    "url":"http://example.com/", # testrail api のURL

    "user":"hoge",               # testrail ユーザ

    "password":"hoge",           # testrail パスワード

    "isDisabled":false,          # このツールを実行しない場合は、trueに変更

    "sync":true,                 # [true] にした場合
                                 # junitテストケースから削除したときに、testrailからも削除する
                                 # [false] にした場合
                                 # junitテストケースから削除したときに、testrailから削除しない

    "isDebugEnabled":false,      # このツールのdebugログを表示する

    "runIds":[
        {
        "target":"com.github.itoshige.testrail", # junitテストケースが存在するパッケージ名を入力する
        "runId":"359"                            # testrailの4で作成したrunIdを入力する
        },
    ]
}
```

### how to use
[1] テストクラスの親クラスに @ClassRule and @Rule を設置する
[このソースを参考](https://github.com/itoshige/testrail-unit/blob/develop/src/test/java/com/github/itoshige/testrail/TestBase.java)
```
@ClassRule
public static TestRailUnit tr = new TestRailUnit();
@Rule
public TestRailStorage ts = new TestRailStorage(tr);
...
```
[2] もし、testrailに反映したくないクラス・メソッドは、@IgnoreTestRail を配置する
[このソースを参考](https://github.com/itoshige/testrail-unit/blob/develop/src/test/java/com/github/itoshige/testrail/rules/TestRailClassAddTest.java)
```
@IgnoreTestRail
@Test
public void Sample_01() {
...

```
[3]. testrailに反映するSection名をクラス名ではなく、任意の値にしたい場合は、@Section を配置する
```
@Section(name = "サンプルセクション ")
public class TestRailClassAddTest extends TestBase {
...
```

## License
* MIT  
    * see LICENSE