import java.util.Locale;

public class Test {
    public static void main(String[] args) {
        Locale locales[] = Locale.getAvailableLocales();
        for (Locale locale:locales) {
            System.out.println(locale.getDisplayCountry()+"="+locale.getCountry()+" "+locale.getDisplayLanguage()+"="+locale.getLanguage());
        }
    }
}
