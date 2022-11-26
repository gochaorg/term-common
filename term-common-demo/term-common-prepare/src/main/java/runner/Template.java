package runner;

public class Template {
    //region applicationName
    private static final String APP_NAME_VARNAME = "APP_NAME";
    private String applicationName = "appName";
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    //endregion
    //region mainClass
    private static final String MAIN_CLASS_VARNAME = "MAIN_CLASS";
    private String mainClass = "mainClass";
    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }
    //endregion
    //region javaExe
    private static final String JAVA_EXE_VARNAME = "JAVA_EXE";
    private String javaExe = "javaw";
    public String getJavaExe() {
        return javaExe;
    }

    public void setJavaExe(String javaExe) {
        this.javaExe = javaExe;
    }
    //endregion
    //region repoName
    private static final String REPO_NAME_VARNAME = "REPO_NAME";
    private String repoName = "repo";

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }
    //endregion
    //region javaOptions
    private final static String JAVA_OPTS = "JAVA_OPTS";
    private String javaOptions = "";

    public String getJavaOptions() {
        return javaOptions;
    }

    public void setJavaOptions(String javaOptions) {
        this.javaOptions = javaOptions;
    }
    //endregion

    private final String source;

    public Template(String source){
        if( source==null )throw new IllegalArgumentException("source == null");
        this.source = source;
    }

    public Template(Template sample){
        if( sample==null )throw new IllegalArgumentException("sample == null");
        this.source = sample.source;
        applicationName = sample.applicationName;
        mainClass = sample.mainClass;
        javaExe = sample.javaExe;
        repoName = sample.repoName;
    }

    public Template clone(){
        return new Template(this);
    }

    public String toString(){
        var sb = new StringBuilder();
        new Heredoc().toString(sb, source, plainText -> plainText, code -> {
            if( JAVA_EXE_VARNAME.equals(code) )return getJavaExe();
            if( MAIN_CLASS_VARNAME.equals(code) )return getMainClass();
            if( REPO_NAME_VARNAME.equals(code) )return getRepoName();
            if( APP_NAME_VARNAME.equals(code) )return getApplicationName();
            if( JAVA_OPTS.equals(code) )return getJavaOptions();
            return "?"+code+"?";
        });
        return sb.toString();
    }
}
