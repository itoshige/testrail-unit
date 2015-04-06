# testrail-unit
## outline
* junitの実行結果を、testrailのテストケースに反映する

## how to use
### preparation
#### testrailにテストケースを作成時に、以下のルールに沿って作成する
* testrailのSection名 と junitテストクラス名 を同じ名前にする
* testrailのtestcase名 と junitテストメソッド名 を同じ名前にする
* projectディレクトリ内に[testrail-unit.properties](https://github.com/itoshige/testrail-unit/blob/develop/src/test/resources/testrail-unit.properties)ファイルを用意する

### how to use
* [こちら](https://github.com/itoshige/testrail-unit/blob/develop/src/test/java/org/itoshige/testrail/annotation/TestRailAnnotationTest.java)を参考

|1. junitテストクラス内に、@Ruleを設置
```
@Rule
public TestRailUnit tu = new TestRailUnit(XXX); // XXXはtestrailのrunIdを入れる
```
|2. testrailに実行結果を反映したいjunitテストメソッドに、@TestRailアノテーションを付与する
```
@Rule
 @TestRail
@Test
public void Sample_01() {
...
```
