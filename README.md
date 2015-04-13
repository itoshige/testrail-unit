# testrail-unit
## -- English --
## outline
* This tool reflect junit test result to testrail.

## how to use
### preparation
#### If you create test case in testrail, you need to observe the rule.
* section name of testrail == junit test class name
* don't define the same section name in the same testcase
* test case name of testrail == junit test method name
* prepare [testrail-unit.properties](https://github.com/itoshige/testrail-unit/blob/develop/src/test/resources/testrail-unit.properties) in your project

### how to use
* [Please check it](https://github.com/itoshige/testrail-unit/blob/develop/src/test/java/org/itoshige/testrail/annotation/TestRailAnnotationTest.java)

|1. set @ClassRule and @Rule in junit test class.
```
@ClassRule
public static TestRailUnit tr = new TestRailUnit(XXX);
@Rule
public TestRailStorage ts = new TestRailStorage(XXX);
...
※XXX: testrail runId
```
|2. If you don't want to reflect junit result to testrail, Please use @IgnoreTestRail
```
@IgnoreTestRail
@Test
public void Sample_01() {
...
```


## -- 日本語 --
## outline
* junitの実行結果を、testrailのテストケースに反映する

## how to use
### preparation
#### testrailにテストケースを作成時に、以下のルールに沿って作成する
* testrailのSection名 と junitテストクラス名 を同じ名前にする
* testcase内に同じSection名を定義しない
* testrailのtestcase名 と junitテストメソッド名 を同じ名前にする
* projectディレクトリ内に[testrail-unit.properties](https://github.com/itoshige/testrail-unit/blob/develop/src/test/resources/testrail-unit.properties)ファイルを用意する

### how to use
* [こちら](https://github.com/itoshige/testrail-unit/blob/develop/src/test/java/org/itoshige/testrail/annotation/TestRailAnnotationTest.java)を参考

|1. junitテストクラス内に、@ClassRuleと@Ruleを設置
```
@ClassRule
public static TestRailUnit tr = new TestRailUnit(XXX);
@Rule
public TestRailStorage ts = new TestRailStorage(XXX);
...
※XXXはtestrailのrunIdを入れる
```
|2. testrailに実行結果を反映したくないjunitテストメソッドは、@IgnoreTestRailアノテーションを付与する
```
@IgnoreTestRail
@Test
public void Sample_01() {
...
```
