package lang.c.parse;

import java.io.PrintStream;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;

public class ident extends CParseRule {
	// ident ::= IDENT
	private CToken ident;
	public ident(CParseContext pcx) {
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_IDENT;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		//System.out.println("identの構文解析中です");
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		ident = tk;
		//System.out.println("ident : " + ident.toExplainString());
		tk = ct.getNextToken(pcx);
		//System.out.println("NextToken : " + tk.toExplainString());
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		//System.out.println("identの意味解析中です");
		if(ident != null) {
			//識別子が何から始まっているのかを判断する
			String[] arr = ident.getText().split("_");
			System.out.println("識別子の接頭語: [" + arr[0] + "]");
			if(arr[0].equals("i")) {
			this.setCType(CType.getCType(CType.T_int));
			this.setConstant(false);
			}else if(arr[0].equals("ip")) {
				this.setCType(CType.getCType(CType.T_pint));
				this.setConstant(false);;
			}else if(arr[0].contentEquals("ia")) {
				this.setCType(CType.getCType(CType.T_aint));
				this.setConstant(false);
			}else if(arr[0].contentEquals("ipa")) {
				this.setCType(CType.getCType(CType.T_apint));
				this.setConstant(false);
			}else if(arr[0].contentEquals("c")) {
				this.setCType(CType.getCType(CType.T_int));
				this.setConstant(true);
			}else {
				this.setCType(CType.getCType(CType.T_int));
				this.setConstant(false);
			}
		}
	}

	public void codeGen(CParseContext pcx) {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; ident starts");
		if (ident != null) {
		//	String[] arr = ident.getText().split("_");
			o.println("\tMOV\t#" + ident.getText() + ", (R6)+\t; Ident: 変数アドレスを積む<"
					+ ident.toExplainString() + ">");
			//if((arr[0].contentEquals("ip") || arr[0].contentEquals("ipa"))) {
			//	o.println("\tMOV\t-(R6), R0\t;\tIdent: スタックから変数アドレスを取り出す");
			//	o.println("\tMOV\t(R0), (R6)+\t;\tIdent: 変数アドレスから値(ポインタなのでアドレス値)を取り出しスタックへ");
			//}
		}
		o.println(";;; ident completes");
	}
}
