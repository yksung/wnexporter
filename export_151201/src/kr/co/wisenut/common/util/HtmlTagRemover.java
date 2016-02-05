package kr.co.wisenut.common.util;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006. 5. 22.
 * Time: ���� 9:42:6
 * To change this template use File | Settings | File Templates.
 */
public class HtmlTagRemover {    
    private static String[] htmlWords = { "&nbsp;", "&lt;", "&gt;", "&quot;", "&amp;" };
    private static int MAX_WORD_LEN = 6;
    private static String CRLF = System.getProperty("line.separator");

    private static void decodeHtmlLastWord(StringBuffer sb, int pos) {
        if (sb.length() < MAX_WORD_LEN)
            return;
        int len = sb.length();
        int len2;
        String str = sb.substring(len - MAX_WORD_LEN);
        for (int i = 0; i < htmlWords.length; i++) {
            if (str.toLowerCase().endsWith(htmlWords[i])) {
                len2 = htmlWords[i].length();
                sb.delete(len - len2, len);
                return;
            }
        }
    }

    public static String decodeHtml(String s) {
        if (s == null)
            return null;
        StringBuffer sb = new StringBuffer();
        char[] chars = s.toCharArray();
        char a;
        for (int i = 0; i < chars.length; i++) {
            a = chars[i];
            switch (a) {
                case ';':
                    decodeHtmlLastWord(sb, sb.length());
                    break;
                default:
                    sb.append(a);
                    break;
            }
        }
        return sb.toString();
    }

    private static String[] nlTags = { "P", "CENTER", "HR", "BR", "LI",
                                       "UL", "/UL", "OL",  "/OL",
                                       "TABLE", "/TABLE", "/TR",
                                       "META", "BODY", "/BODY", "/HTML",
                                       "/HEAD", "/TITLE" };

    public static boolean isNewLineTag(String s) {
        if (s == null)
            return false;
        String str = allTrim(s);
        for (int i = 0; i < nlTags.length; i++) {
            if (str.equalsIgnoreCase(nlTags[i]))
                return true;
        }
        return false;
    }


    public String removeHtmlTag(String s) {
        final int NORMAL_STATE = 0;
        final int TAG_STATE = 1;
        final int START_TAG_STATE = 2;
        final int END_TAG_STATE = 3;
        final int SINGLE_QUOT_STATE = 4;
        final int DOUBLE_QUOT_STATE = 5;
        int state = NORMAL_STATE;
        int oldState = NORMAL_STATE;
        char[] chars = s.toCharArray();
        StringBuffer sb = new StringBuffer();
        boolean bSlash = false;
        String tagWord = "";
        boolean tagWordDone = false;
        char a;
        for (int i = 0; i < chars.length; i++) {
            a = chars[i];
            switch (state) {
                case NORMAL_STATE:
                    if (a == '<') {
                        tagWord = "";
                        state = TAG_STATE;
                    }
                    else
                        sb.append(a);
                    break;
                case TAG_STATE:
                    if (a == '>')
                        state = NORMAL_STATE;
                    else if (a == '\"') {
                        oldState = state;
                        state = DOUBLE_QUOT_STATE;
                    }
                    else if (a == '\'') {
                        oldState = state;
                        state = SINGLE_QUOT_STATE;
                    }
                    else if (a == '/') {
                        tagWord = "" + a;
                        tagWordDone = false;
                        state = END_TAG_STATE;
                    }
                    else if (a != ' ' && a != '\t' && a != '\n' && a != '\r' && a != '\f') {
                        tagWord = "" + a;
                        tagWordDone = false;
                        state = START_TAG_STATE;
                    }
                    break;
                case START_TAG_STATE:
                case END_TAG_STATE:
                    if (a == '>') {
                        if (isNewLineTag(tagWord)) {
                            sb.append(CRLF);
                        }
                        state = NORMAL_STATE;
                    }
                    else if (a == '\"') {
                        oldState = state;
                        state = DOUBLE_QUOT_STATE;
                    }
                    else if (a == '\'') {
                        oldState = state;
                        state = SINGLE_QUOT_STATE;
                    }
                    else if (a == ' ' || a == '\t' || a == '\n' || a == '\r' || a == '\f') {
                        tagWordDone = true;
                    }
                    else if (!tagWordDone) {
                        tagWord += a;
                    }
                    break;
                case DOUBLE_QUOT_STATE:
                    if (bSlash)
                        bSlash = false;
                    else if (a == '\"')
                        state = oldState;
                    else if (a == '\\')
                        bSlash = true;
                    break;
                case SINGLE_QUOT_STATE:
                    if (bSlash)
                        bSlash = false;
                    else if (a == '\'')
                        state = oldState;
                    else if (a == '\\')
                        bSlash = true;
                    break;
            }
        }
        String strHtml = sb.toString();
        strHtml = strHtml.replace('\t',' ');
        strHtml = strHtml.replace('\r',' ');
        strHtml = strHtml.replace('\n',' ');
        return strHtml;
    }

    public static String allTrim(String s) {
        if (s == null)
            return null;
        else if (s.length() == 0)
            return "";

        int len = s.length();
        int i = 0;
        int j = len - 1;

        for (i = 0; i < len; i++) {
            if ( s.charAt(i) != ' ' && s.charAt(i) != '\t'
                    && s.charAt(i) != '\r'
                    && s.charAt(i) != '\n' )
                break;
        }
        if (i == len)
            return "";

        for (j = len - 1; j >= i; j--) {
            if ( s.charAt(i) != ' ' && s.charAt(i) != '\t'
                    && s.charAt(i) != '\r'
                    && s.charAt(i) != '\n' )
                break;
        }
        return s.substring(i, j + 1);
    }

    public static void main(String[] args) {
        String page3 ="";
        String page2="<DIV style=\"FONT-SIZE: 10pt; FONT-FAMILY: ����ü\">&nbsp;<FONT style=\"FONT-SIZE: 16pt\">\"����� ����Ȳ�ڷ� ÷���Դϴ�.\"</FONT></DIV>";
        String page =
                    "<table id=truebody width=630 border=1 cellspacing=0 bordercolordark=BBCAC5 bordercolorlight=BBCAC5 style=\"border:0;\">\n" +
                    "  <tr id=trshow height=250 valign=top><td id=bodyshow style=display:;>\n" +
                    "<BR>\n" +
                    "\n" +
                    "<P><P>\n" +
                    "<P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;---&amp;</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">ǥf�� �λ��� �ٷγ� �����('03. 5. 9����)�� ����Ͽ� �Ʒ��� ���� �� ����� �ٷγ� ID �߱�; ��û�帮�4�, vġ�Ͽ� �ֽñ� �ٶ�ϴ�.</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\" align=center>- �� &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� -</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">1. ����� �������</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;&nbsp;&nbsp;1) �� &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� : O060660</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;&nbsp;&nbsp;2) �� &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� : ��dö</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;&nbsp;&nbsp;3) �ֹι�ȣ : 570125-1063516</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;&nbsp;&nbsp;4) �� &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;~ : �߱�(����)</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">2. ���� b�� E-Mail address : jck125@yahoo.co.kr</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;</P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">3. ��û ��/ : �� ������ �÷�Ʈ �� �߱�� ����ڷμ� ����/��ǰ û����; </P>\n" +
                    "<P style=\"MARGIN-TOP: 0px; MARGIN-BOTTOM: 0px; TEXT-INDENT: 2px\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����� �뺻�� ��� �ٷγ� b���� ��u�. &nbsp;-��.-&nbsp;</P>\n" +
                    "\n" +
                    "  </td></tr>\n" +
                    "</table>";

        HtmlTagRemover htr = new HtmlTagRemover();
        System.out.println(htr.removeHtmlTag(page3));

    }
}
