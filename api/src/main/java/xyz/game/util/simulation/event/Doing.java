package xyz.game.util.simulation.event;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Doing {
  /**
   * A表示实施对象，B表示作用对象
   * A.X >= 100 || B.Y.currentV < B.Y.maxV 以这个为例，都是 对象的某个属性的某个值 判断是否 大于、小于等等的 大小判断条件
   * 可以是或者、并且的关系组合，可以使用括号
   */
  private String condition;

  private List<String> dicrThing;
  private List<String> incrThing;

  /**
   * 物理伤害、魔法伤害、治疗效果、移速降低、最大生命值减少、最大生命值增加等等，不同的类型对应不同的计算公式，注意要跟condition区分开
   * 例如物理伤害对应的物理伤害计算公式、魔法伤害对应的魔法伤害计算公式、治疗效果对应的治疗效果计算公式、真实伤害也可能有系数需要乘
   * 暴击也根据type选择不同的公式处理
   * 友方技能还是敌方技能
   * 全体还是非全体等等
   */
  private Set<Integer> types;

  // 订阅事件 触发护甲减少、攻击特效、装备效果等等；顺序：before->this->after
  private List<BaseEvent> subBeforeEvents;
  private List<BaseEvent> subAfterEvents;

  /**
   * 评估条件表达式并返回最终结果
   * @return 条件表达式的最终结果
   */
  public boolean evaluateCondition() {
    // 首先解析条件
    parseCondition();

    // 在实际应用中，这里应该将解析后的条件表达式转换为布尔表达式
    // 这里简化处理，直接使用原始条件表达式
    // 实际应用中，您需要将每个单元表达式评估为true或false，然后组合成完整的布尔表达式

    // 示例：假设我们已经将条件表达式转换为布尔表达式
    // 实际应用中，您需要根据解析出的单元表达式构建这个布尔表达式
    String booleanExpression = condition; // 这里应该是转换后的布尔表达式

    // 评估布尔表达式
    return evaluateBooleanExpression(booleanExpression);
  }

  /**
   * 解析condition
   * condition是由一堆括号包围的表达式构成的，括号之间用|或&连接，括号内可以有别的表达式，也可以是简单的属性判断
   */
  public void parseCondition() {
    if (condition == null || condition.isEmpty()) {
      return;
    }

    // 用于存储括号的栈
    java.util.Stack<Integer> bracketStack = new java.util.Stack<>();
    // 存储解析出的单元表达式
    java.util.List<String> unitExpressions = new java.util.ArrayList<>();

    // 遍历条件字符串，使用栈来处理括号
    for (int i = 0; i < condition.length(); i++) {
      char c = condition.charAt(i);

      // 处理左括号
      if (c == '(') {
        bracketStack.push(i);
      }
      // 处理右括号，当遇到右括号时，提取括号内的表达式
      else if (c == ')') {
        if (!bracketStack.isEmpty()) {
          int startIndex = bracketStack.pop();
          // 提取括号内的表达式
          String subExpr = condition.substring(startIndex + 1, i).trim();
          if (!subExpr.isEmpty()) {
            // 处理括号内的单元表达式
            processUnitExpressions(subExpr, unitExpressions);
          }
        }
      }
    }

    // 处理没有括号的顶层表达式
    if (bracketStack.isEmpty()) {
      processUnitExpressions(condition, unitExpressions);
    }

    // 这里可以将解析结果保存起来，供后续使用
    System.out.println("解析出的单元表达式: " + unitExpressions);
  }

  /**
   * 评估布尔表达式并返回最终结果
   * @param expression 布尔表达式，如 "true&(false|true)|((false|true)&false)"
   * @return 表达式的最终结果
   */
  public boolean evaluateBooleanExpression(String expression) {
    if (expression == null || expression.isEmpty()) {
      return false;
    }

    // 去除所有空格
    expression = expression.replaceAll("\\s+", "");

    // 使用栈来处理括号和运算符优先级
    java.util.Stack<Boolean> valueStack = new java.util.Stack<>();
    java.util.Stack<Character> operatorStack = new java.util.Stack<>();

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);

      if (c == 't') { // 处理 "true"
        valueStack.push(true);
        i += 3; // 跳过 "rue"
      } else if (c == 'f') { // 处理 "false"
        valueStack.push(false);
        i += 4; // 跳过 "alse"
      } else if (c == '(') { // 左括号压入操作符栈
        operatorStack.push(c);
      } else if (c == ')') { // 右括号，计算括号内的表达式
        while (operatorStack.peek() != '(') {
          valueStack.push(applyOperator(operatorStack.pop(), valueStack.pop(), valueStack.pop()));
        }
        operatorStack.pop(); // 弹出左括号
      } else if (c == '&' || c == '|') { // 处理逻辑操作符
        // 当前操作符优先级小于等于栈顶操作符时，先计算栈顶操作符
        while (!operatorStack.isEmpty() && hasPrecedence(c, operatorStack.peek())) {
          valueStack.push(applyOperator(operatorStack.pop(), valueStack.pop(), valueStack.pop()));
        }
        operatorStack.push(c);
      }
    }

    // 处理剩余的操作符
    while (!operatorStack.isEmpty()) {
      valueStack.push(applyOperator(operatorStack.pop(), valueStack.pop(), valueStack.pop()));
    }

    // 最终结果应该在值栈的顶部
    return valueStack.pop();
  }


  /**
   * 判断操作符优先级
   * @param op1 当前操作符
   * @param op2 栈顶操作符
   * @return 如果栈顶操作符优先级大于等于当前操作符，返回true
   */
  private boolean hasPrecedence(char op1, char op2) {
    if (op2 == '(' || op2 == ')') {
      return false;
    }
    // & 和 | 优先级相同，从左到右计算
    return true;
  }

  /**
   * 应用逻辑操作符
   * @param operator 操作符 ('&' 或 '|')
   * @param b 第二个操作数
   * @param a 第一个操作数
   * @return 计算结果
   */
  private boolean applyOperator(char operator, boolean b, boolean a) {
    switch (operator) {
      case '&':
        return a && b;
      case '|':
        return a || b;
      default:
        throw new IllegalArgumentException("未知的操作符: " + operator);
    }
  }

  /**
   * 处理单元表达式，识别比较操作符和属性路径
   */
  private void processUnitExpressions(String expression, java.util.List<String> unitExpressions) {
    // 去除空格，便于解析
    expression = expression.replaceAll("\\s+", "");

    // 定义可能的比较操作符，按长度排序以确保优先匹配较长的操作符
    String[] operators = {">=", "<=", "==", "!=", ">", "<"};

    // 检查是否包含逻辑操作符
    if (expression.contains("|") || expression.contains("&")) {
      // 这里简化处理，实际可能需要更复杂的词法分析来正确处理嵌套的逻辑表达式
      // 分割逻辑表达式
      String[] parts = expression.split("[|&]");
      for (String part : parts) {
        part = part.trim();
        if (!part.isEmpty()) {
          unitExpressions.add(part);
        }
      }
    } else {
      // 检查是否包含比较操作符
      for (String op : operators) {
        if (expression.contains(op)) {
          String[] parts = expression.split(op);
          if (parts.length == 2) {
            String left = parts[0].trim();
            String right = parts[1].trim();

            // 解析左侧属性路径 (两个点分隔的对象属性)
            parsePropertyPath(left);

            // 解析右侧属性路径或计算表达式
            parseRightExpression(right);

            unitExpressions.add(expression);
            break;
          }
        }
      }
    }
  }

  /**
   * 解析左侧属性路径 (格式为A.X.Y这样的两个点分隔的路径)
   */
  private void parsePropertyPath(String path) {
    // 检查路径是否符合两个点分隔的格式
    String[] parts = path.split("\\.");
    if (parts.length >= 3) {
      String object = parts[0]; // 对象名 (如A或B)
      String property1 = parts[1]; // 第一个属性
      String property2 = parts[2]; // 第二个属性

      // 这里可以根据需要保存解析结果
      System.out.println("解析左侧属性路径: 对象=" + object + ", 属性1=" + property1 + ", 属性2=" + property2);
    }
  }

  /**
   * 解析右侧表达式，可以是属性路径或计算表达式
   */
  private void parseRightExpression(String expression) {
    // 检查是否是属性路径 (包含点号)
    if (expression.contains(".")) {
      // 类似于左侧属性路径的解析
      parsePropertyPath(expression);
    } else {
      // 尝试解析为数值或其他计算表达式
      try {
        // 简单的数值解析，实际可能需要更复杂的表达式计算
        // todo +-*/等等，可能要引入别的框架
        if (expression.contains("+")) {
          String[] addParts = expression.split("\\+");
          int sum = 0;
          for (String part : addParts) {
            sum += Integer.parseInt(part.trim());
          }
          System.out.println("解析右侧加法表达式: " + expression + " = " + sum);
        } else if (expression.contains("-")) {
          String[] subParts = expression.split("-");
          int result = Integer.parseInt(subParts[0].trim());
          for (int i = 1; i < subParts.length; i++) {
            result -= Integer.parseInt(subParts[i].trim());
          }
          System.out.println("解析右侧减法表达式: " + expression + " = " + result);
        } else {
          // 单个数值
          int value = Integer.parseInt(expression);
          System.out.println("解析右侧数值: " + value);
        }
      } catch (NumberFormatException e) {
        // 不是数值，可能是字符串或其他类型
        System.out.println("解析右侧非数值表达式: " + expression);
      }
    }
  }
}
