# testrail-unit
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
