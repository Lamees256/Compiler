package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SampleController {

	static ArrayList<String> keywords = new ArrayList<String>();
	static ArrayList<String> symbols = new ArrayList<String>();
	static ArrayList<String> tokens = new ArrayList<String>();
	static ArrayList<String> nonTerminals = new ArrayList<>();
	static HashMap<String, Integer> map = new HashMap<String, Integer>();
	static ArrayList<Token> idTokens = new ArrayList<Token>();
	static ArrayList<Token> saq = new ArrayList<Token>();
	static Stack<String> stack = new Stack<String>();
	static int lineNumber = 0;

	public static void makeKeyWordsTable() {
		keywords.add("project");
		keywords.add("const");
		keywords.add("var");
		keywords.add("int");
		keywords.add("subroutine");
		keywords.add("begin");
		keywords.add("end");
		keywords.add("scan");
		keywords.add("print");
		keywords.add("if");
		keywords.add("then");
		keywords.add("endif");
		keywords.add("else");
		keywords.add("while");
		keywords.add("do");
//		for(String word : keywords) {
//			System.out.println(word +" "+ keywords.indexOf(word));
//		}
	}

	public static void makeSymbolsTable() {
		symbols.add(".");
		symbols.add(";");
		symbols.add(":");
		symbols.add(",");
		symbols.add(":=");
		symbols.add("(");
		symbols.add(")");
		symbols.add("+");
		symbols.add("-");
		symbols.add("*");
		symbols.add("/");
		symbols.add("%");
		symbols.add("=");
		symbols.add("|=");
		symbols.add("<");
		symbols.add("=<");
		symbols.add(">");
		symbols.add("=>");
	}

	public static void chooseFile() {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("txt files", "*.txt"));
		File file = fc.showOpenDialog(null);
		try {
			Scanner scanner;
			scanner = new Scanner(file);
			// validChar(file);

			makeVnList();
			createParsingTable();
			readFile(file);

		} catch (FileNotFoundException e1) {
			System.out.println();
		} catch (java.lang.NullPointerException e2) {
			System.out.println("No File selected");
		}
	}

	public static void makeVnList() {
		nonTerminals.add("project-declaration");
		nonTerminals.add("project-def");
		nonTerminals.add("project-heading");
		nonTerminals.add("declarations");
		nonTerminals.add("const-decl");
		nonTerminals.add("const-list");
		nonTerminals.add("var-decl");
		nonTerminals.add("var-list");
		nonTerminals.add("var-item");
		nonTerminals.add("name-list");
		nonTerminals.add("more-names");
		nonTerminals.add("subroutine-decl");
		nonTerminals.add("subroutine-heading");
		nonTerminals.add("compound-stmt");
		nonTerminals.add("stmt-list");
		nonTerminals.add("statement");
		nonTerminals.add("ass-stmt");
		nonTerminals.add("arith-exp");
		nonTerminals.add("arith-exp-prime");
		nonTerminals.add("term");
		nonTerminals.add("term-prime");
		nonTerminals.add("factor");
		nonTerminals.add("name-value");
		nonTerminals.add("add-sign");
		nonTerminals.add("mul-sign");
		nonTerminals.add("inout-stmt");
		nonTerminals.add("if-stmt");
		nonTerminals.add("else-part");
		nonTerminals.add("while-stmt");
		nonTerminals.add("bool-exp");
		nonTerminals.add("relational-oper");

	}

	public static void readFile(File file) throws FileNotFoundException {
		ArrayList<String> tokens = new ArrayList<>();
		int lineNum = 0;
		String num = "";
		String word = "";
		if (file.exists()) {
			Scanner in;
			try {
				in = new Scanner(file);
				while (in.hasNextLine()) {
					String string = in.nextLine().trim();
					lineNum++;
//					System.out.println(string);
					try {
						for (int i = 0; i < string.length(); i++) {
							char ch = string.charAt(i);

							if (validChar(ch) == false) {
								System.out.println("Illegal Character Dectected " + ch);
								return;
							}
							try {
								if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z') {
									// System.out.println((char)ch);
									word = "";
									num = "";
									while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
											|| (ch >= '0' && ch <= '9')) {
										word = word + (char) ch;
										ch = string.charAt(i + 1);
										i++;
									}
									// System.out.println(word);
									if (keywords.contains(word)) {
										int id = keywords.indexOf(word);
										Token tok = new Token(word, id, lineNum);
										idTokens.add(tok);
									} else {
										Token tok = new Token(word, 1999, lineNum);
										idTokens.add(tok);
									}
									tokens.add(word);

									// System.out.println("its a letter"); // concat all letters together until you
									// reach a space
								}
								if (ch >= '0' && ch <= '9') {
									word = "";
									// num = "";
									while (ch >= '0' && ch <= '9') {
										num = num + (char) ch;
										ch = string.charAt(i + 1);
										i++;
									}

									Token t = new Token(num, 9090, lineNum);
									idTokens.add(t);

									tokens.add(num);
									num = "";

								}
							} catch (Exception e) {

								if (word != "" && i == string.length() - 1) { // debug here to see the value of i at
									if (keywords.contains(word)) {
										int id = keywords.indexOf(word);
										Token tok = new Token(word, id, lineNum);
										idTokens.add(tok);
									} else {
										Token tok = new Token(word, 1999, lineNum);
										idTokens.add(tok);
									} // this point
									tokens.add(word);
								}
//
								if (num != "" && i == string.length() - 1) {
									Token t = new Token(num, 9090, lineNum);
									idTokens.add(t);

									tokens.add(num);
								}
							}

							int n;
							Token to;

							switch (ch) {

							case '.':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case ';':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case ',':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case '(':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case ')':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case '+':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case '-':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case '*':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case '/':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case '%':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case ':':
								if (string.charAt(i + 1) == '=') {
									n = symbols.indexOf(":=");
									to = new Token(":=", n, lineNum);
									idTokens.add(to);
									tokens.add(":=");
									i++;
								} else {
									n = symbols.indexOf(ch + "");
									to = new Token(ch + "", n, lineNum);
									idTokens.add(to);
									tokens.add(ch + "");
								}
								break;
							case '|':
								if (string.charAt(i + 1) == '=') {
									n = symbols.indexOf("|=");
									to = new Token("|=", n, lineNum);
									idTokens.add(to);
									tokens.add("|=");
									i++;
								} else {
									System.out.println("Line " + lineNum + ": Error add '=' after '|'");
									return;
								}
								break;
							case '=':
								if (string.charAt(i + 1) == '<') {
									n = symbols.indexOf("=<");
									to = new Token("=<", n, lineNum);
									idTokens.add(to);
									tokens.add("=<");
									i++;
								} else if (string.charAt(i + 1) == '>') {
									n = symbols.indexOf("=>");
									to = new Token("=>", n, lineNum);
									idTokens.add(to);
									tokens.add("=>");
									i++;
								} else {
									n = symbols.indexOf(ch + "");
									to = new Token(ch + "", n, lineNum);
									idTokens.add(to);
									tokens.add(ch + "");
								}

								break;
							case '>':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							case '<':
								n = symbols.indexOf(ch + "");
								to = new Token(ch + "", n, lineNum);
								idTokens.add(to);
								tokens.add(ch + "");
								break;
							}

						}

					} catch (Exception e) {

					}
				}
			} catch (FileNotFoundException e1) {

			}
		}

//		for (String token : tokens) {
//			System.out.println(token);
//		}
		for (Token token : idTokens) {
			System.out.println(token);
		}
		saq = idTokens;
		// lineNumber = lineNum - 1;
		check();
	}

	public static void createParsingTable() {

		map.put("project-declaration&project", 1);
		map.put("project-def&project", 2);
		map.put("project-heading&project", 3);

		map.put("declarations&const", 4);
		map.put("declarations&var", 4);
		map.put("declarations&subroutine", 4);
		map.put("declarations&begin", 4);

		map.put("const-decl&const", 5);
		map.put("const-decl&var", 6);
		map.put("const-decl&subroutine", 6);
		map.put("const-decl&begin", 6);

		map.put("const-list&var", 8);
		map.put("const-list&const-name", 7);
		map.put("const-list&subroutine-heading", 8);
		map.put("const-list&begin", 8);

		map.put("var-decl&var", 9);
		map.put("var-decl&subroutine", 10);
		map.put("var-decl&begin", 10);

		map.put("var-list&subroutine", 12);
		map.put("var-list&begin", 12);
		map.put("var-list&var-name", 11);

		map.put("var-item&var-name", 13);

		map.put("name-list&var-name", 14);

		map.put("more-names&:", 16);
		map.put("more-names&,", 15);

		map.put("subroutine-decl&subroutine", 17);
		map.put("subroutine-decl&begin", 18);
		map.put("subroutine-heading&subroutine", 19);
		map.put("compound-stmt&begin", 20);

		map.put("stmt-list&begin", 21);
		map.put("stmt-list&end", 22);
		map.put("stmt-list&scan", 21);
		map.put("stmt-list&print", 21);
		map.put("stmt-list&if", 21);
		map.put("stmt-list&endif", 21);
		map.put("stmt-list&else", 21);
		map.put("stmt-list&while", 21);
		map.put("stmt-list&;", 21);
		map.put("stmt-list&name", 21);

		map.put("statement&begin", 27);
		map.put("statement&scan", 24);
		map.put("statement&if", 25);
		map.put("statement&endif", 28);
		map.put("statement&else", 28);
		map.put("statement&while", 26);
		map.put("statement&;", 28);
		map.put("statement&name", 23);
		map.put("statement&print", 24);

		map.put("ass-stmt&name", 29);

		map.put("arith-exp&(", 30);
		map.put("arith-exp&name", 30);
		map.put("arith-exp&integer-value", 30);

		map.put("arith-exp-prime&endif", 32);
		map.put("arith-exp-prime&else", 32);
		map.put("arith-exp-prime&;", 32);
		map.put("arith-exp-prime&+", 31);
		map.put("arith-exp-prime&-", 31);
		map.put("arith-exp-prime&)", 32);

		map.put("term&(", 33);
		map.put("term&name", 33);
		map.put("term&integer-value", 33);

		map.put("term-prime&endif", 35);
		map.put("term-prime&else", 35);
		map.put("term-prime&;", 35);
		map.put("term-prime&+", 35);
		map.put("term-prime&-", 35);
		map.put("term-prime&*", 34);
		map.put("term-prime&/", 34);
		map.put("term-prime&%", 34);
		map.put("term-prime&)", 35);

		map.put("factor&(", 36);
		map.put("factor&name", 37);
		map.put("factor&integer-value", 37);

		map.put("name-value&name", 38);
		map.put("name-value&integer-value", 39);

		map.put("add-sign&+", 40);
		map.put("add-sign&-", 41);

		map.put("mul-sign&*", 42);
		map.put("mul-sign&/", 43);
		map.put("mul-sign&%", 44);

		map.put("inout-stmt&scan", 45);
		map.put("inout-stmt&print", 46);

		map.put("if-stmt&if", 47);

		map.put("else-part&endif", 49);
		map.put("else-part&else", 48);

		map.put("while-stmt&while", 50);

		map.put("bool-exp&name", 51);
		map.put("bool-exp&integer-value", 51);

		map.put("relational-oper&=", 52);
		map.put("relational-oper&|=", 53);
		map.put("relational-oper&<", 54);
		map.put("relational-oper&=<", 55);
		map.put("relational-oper&>", 56);
		map.put("relational-oper&=>", 57);

	}

	public static String productionRules(int num) { // replace the input string with whatever the rule says
		switch (num) {
		case 1:
			return "project-def" + " .";
		case 2:
			return "project-heading declarations compound-stmt";
		case 3:
			return "project name ;";
		case 4:
			return "const-decl var-decl subroutine-decl";
		case 5:
			return "const const-list";
		case 6:
			return "";
		case 7:
			return "const-name = integer-value ; const-list";
		case 8:
			return "";
		case 9:
			return "var var-list";
		case 10:
			return "";
		case 11:
			return "var-item ; var-list";
		case 12:
			return "";
		case 13:
			return "name-list : int";
		case 14:
			return "var-name more-names";
		case 15:
			return ", name-list";
		case 16:
			return "";
		case 17:
			return "subroutine-heading declarations compound-stmt ; ";
		case 18:
			return "";
		case 19:
			return "subroutine name ;";
		case 20:
			return "begin stmt-list end";
		case 21:
			return "statement ; stmt-list";
		case 22:
			return "";
		case 23:
			return "ass-stmt";
		case 24:
			return "inout-stmt";
		case 25:
			return "if-stmt";
		case 26:
			return "while-stmt";
		case 27:
			return "compound-stmt";
		case 28:
			return "";
		case 29:
			return "name := arith-exp";
		case 30:
			return "term arith-exp-prime";
		case 31:
			return "add-sign term arith-exp-prime";
		case 32:
			return "";
		case 33:
			return "factor term-prime";
		case 34:
			return "mul-sign factor term-prime";
		case 35:
			return "";
		case 36:
			return "( arith-exp )";
		case 37:
			return "name-value";
		case 38:
			return "name";
		case 39:
			return "integer-value";
		case 40:
			return "+";
		case 41:
			return "-";
		case 42:
			return "*";
		case 43:
			return "/";
		case 44:
			return "%";
		case 45:
			return "scan ( name )";
		case 46:
			return " print ( name-value )";
		case 47:
			return "if bool-exp then statement else-part endif";
		case 48:
			return "else statement";
		case 49:
			return "";
		case 50:
			return "while bool-exp do statement";
		case 51:
			return "name-value relational-oper name-value";
		case 52:
			return "=";
		case 53:
			return "|=";
		case 54:
			return "<";
		case 55:
			return "=<";
		case 56:
			return ">";
		case 57:
			return "=>";
		default:
			return "";
		}

	}

	public static void check() {
		ArrayList<Token> save = idTokens;
		ArrayList<Token> input = idTokens;
		// Stack<String> stack = new Stack<String>();
		stack.add("project-declaration");

		while (!stack.isEmpty()) {
			if (input.isEmpty()) {
				System.out.println("ERROR: missing input" + " Left in stack-> " + stack);
				return;
			}
			try {
				String str = "";
				if (stack.peek().equals("integer-value")) {
					if (input.get(0).getId() == 9090) {
						stack.pop();
						input.remove(0);
						System.out.println("Pop and Advance");
					}
				}

				if (keywords.contains(stack.peek())) {
					if (input.get(0).getId() == 1999) {
						if (stack.peek().equals(input.get(0).getString())) {
							stack.pop();
							input.remove(0);
							System.out.println("Pop and Advance");
						}
					}
				}
//				if (stack.peek().equals("factor")) {
//					bug();
//					System.out.println(stack);
//				}
//				if(stack.peek().equals("else-part")) {
//					if(input.get(0).getString().equals("else")) {
//						int num = map.get(stack.peek() + "&else");
//						action(num);
//					}else if(input.get(0).getString().equals("endif")) {
//						int num = map.get(stack.peek() + "&else");
//						action(num);
//					}else {
//						
//					}
//				}
				if (stack.peek().equals("const-list")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&const-name");
						action(num);
					}
				}
				if (stack.peek().equals("var-list")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&var-name");
						action(num);
					}
				}
				if (stack.peek().equals("var-item")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&var-name");
						action(num);
					}
				}
				if (stack.peek().equals("name-list")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&var-name");
						action(num);
					}
				}

				if (stack.peek().equals("stmt-list")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&name");
						action(num);
					}
				}
				if (stack.peek().equals("statement")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&name");
						action(num);
					}
				}
				if (stack.peek().equals("ass-stmt")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&name");
						action(num);
					}
				}

				if (stack.peek().equals("bool-exp")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&name");
						action(num);
					} else {
						if (input.get(0).getId() == 9090) {
							int num = map.get(stack.peek() + "&integer-value");
							action(num);
						}
					}
				}
				if (stack.peek().equals("arith-exp")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&name");
						action(num);
					} else {
						if (input.get(0).getId() == 9090) {
							int num = map.get(stack.peek() + "&integer-value");
							action(num);
						}
					}
				}
				if (stack.peek().equals("term")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&name");
						action(num);
					} else {
						if (input.get(0).getId() == 9090) {
							int num = map.get(stack.peek() + "&integer-value");
							action(num);
						}
					}
				}
				if (stack.peek().equals("factor")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&name");
						action(num);
					} else {
						if (input.get(0).getId() == 9090) {
							int num = map.get(stack.peek() + "&integer-value");
							action(num);
						}
					}
				}
				if (stack.peek().equals("name-value")) {
					if (input.get(0).getId() == 1999) {
						int num = map.get(stack.peek() + "&name");
						action(num);
					} else {
						if (input.get(0).getId() == 9090) {
							int num = map.get(stack.peek() + "&integer-value");
							action(num);
						}
					}
				}
				if (stack.peek().equals("name") || stack.peek().equals("const-name")
						|| stack.peek().equals("var-name")) {
					if (input.get(0).getId() == 1999 || keywords.contains(input.get(0).getString())) {
						stack.pop();
						input.remove(0);
						System.out.println("Pop and Advance");
					}
				}
				if (stack.peek().equals("integer-value")) {
					if (input.get(0).getId() == 9090) {
						stack.pop();
						input.remove(0);
						System.out.println("Pop and Advance");
					}
				}
				if (stack.peek().equals(input.get(0).getString())) {
					stack.pop();
					input.remove(0);
					System.out.println("Pop and Advance");
				} else if (map.containsKey(stack.peek() + "&" + input.get(0).getString())) {
					int num = map.get(stack.peek() + "&" + input.get(0).getString());
					stack.pop();
					System.out.println(num);
					String rule = productionRules(num);
					String ruleTokens[] = rule.split(" ");
					for (int counter = ruleTokens.length - 1; counter >= 0; counter--) {
						if (!ruleTokens[counter].trim().equals("")) {
							stack.push(ruleTokens[counter].trim());
						}
					}
				} else {
					System.out.println("ERROR @Line : " + input.get(0).getLineNum() + " or before line at input: "
							+ input.get(0).toString());
//					System.out.println(stack.peek());
//					System.out.println(input.get(0).getString());
					return;
				}
			} catch (Exception e) {

			}
		}
		if (input.isEmpty()) {
			System.out.println("No Errors: Successful Parsing");
		}
	}

	public static void action(int num) {
		stack.pop();
		System.out.println(num);
		String rule = productionRules(num);
		String ruleTokens[] = rule.split(" ");
		for (int counter = ruleTokens.length - 1; counter >= 0; counter--) {
			if (!ruleTokens[counter].trim().equals("")) {
				stack.push(ruleTokens[counter].trim());
			}
		}

	}

	public static void bug() {
		System.out.println("hf");
	}

	public static boolean validChar(char ch) {
		switch (ch) {
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z':
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case '.':
		case ';':
		case ':':
		case ',':
		case '(':
		case ')':
		case '+':
		case '-':
		case '*':
		case '/':
		case '%':
		case '=':
		case '|':
		case '<':
		case '>':
		case ' ':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return true;
		default:
			return false;
		}
	}

}
