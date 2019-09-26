 package entry;

 import com.inra.coby.blz.manager.Utils;
 import com.inra.coby.blz.manager.DataLoader;
 import static com.inra.coby.blz.manager.Utils.getProperty;
 import static com.inra.coby.blz.manager.Utils.removeLastSlash;

 public class Main {

    /* EX :
     java -DUrl="http://localhost:9999/blazegraph/namespace/kb/sparql"                                                  \
          -DVerbose=true                                                          \
          -DFileOrDir='/home/ryahiaoui/Téléchargements/coby/coby_standard_bin/pipeline/libs/Blazegraph/SPLIT/data_ac'   \
          -DMediaType='text/rdf+n3'                          \
          -DNamespace=kb                                     \
          -DConnectTimeOut=60                                \
          -DWriteTimeOut=60                                  \
          -DReadTimeOut=60                                   \
          -jar data-loader-1.0-jar-with-dependencies.jar
    */
            
    public static void main(String[] args) throws Exception {
        
        if( System.getProperty("H") != null   || System.getProperty("Help") != null )              {
           System.out.println("                                                                 ") ;
           System.out.println(" ################################################################") ;
           System.out.println(" ### Data-Loader#  ##############################################") ;
           System.out.println(" ----------------------------------------------------------------") ;
           System.out.println(" Total Arguments  :  7                                           ") ;
           System.out.println("   FileOrDir      : Directory or File to Load                    ") ;
           System.out.println("   MediaType      : Default text/rdf+n3                          ") ;
           System.out.println("   Url            : EndPoint.                                    ") ;
           System.out.println("                    Default :  http://localhost:9999/blazegraph/ ") ;
           System.out.println("   Namespace      : Namespace                                    ") ;
           System.out.println("   ConnectTimeOut : In Seconds. Default 60                       ") ;
           System.out.println("   WriteTimeOut   : In Seconds. Default 60                       ") ;
           System.out.println("   ReadTimeOut    : In Seconds. Default 60                       ") ;
           System.out.println("   RemoveOnLoad   : Reamove Loaded File                          ") ;
           System.out.println("   V              : Verbose                                      ") ;
           System.out.println(" ----------------------------------------------------------------") ;
           System.out.println(" ################################################################") ;
           System.out.println("                                                                 ") ;
           System.exit(0)    ;
        }
        
        String fileOrDir       = getProperty("FileOrDir")          ;
        String _mediaType      = getProperty("MediaType")          ;
        String _url            = getProperty("Url")                ;
        String namespace       = getProperty("Namespace")          ;
        String _connectTimeOut = getProperty("ConnectTimeOut")     ;
        String _writeTimeOut   = getProperty("WriteTimeOut")       ;
        String _readTimeOut    = getProperty("ReadTimeOut")        ;
        
        String _verbose        = getProperty("Verbose")            ;
        boolean verbose        = ! _verbose.isEmpty() && 
                                 _verbose.equalsIgnoreCase("true") ;
        
        String _removeOnLoad   = getProperty("RemoveOnLoad")       ;
        boolean removeOnLoad   = ! _removeOnLoad.isEmpty() && 
                                 _removeOnLoad.equalsIgnoreCase("true") ;
        
        if( fileOrDir.trim().isEmpty() )  {
           System.out.println(" \n FileOrDir Not Provided !! \n ") ;
           System.exit(0)                                          ;
        }
        
        if( _url.trim().isEmpty() ) {
           System.out.println(" \n Url Not Provided !! \n ")       ;
           System.exit(0)                                          ;
        }
        
        String url = removeLastSlash(_url )                        ;
        
        if( ! namespace.trim().isEmpty()   && 
            ! url.contains("/namespace/"))  {
              url += "/namespace/" + namespace + "/sparql"         ;
        }
        
        Integer connectTimeOut = Utils.setDefault( _connectTimeOut, 60 )        ;
        Integer readTimeOut    = Utils.setDefault( _readTimeOut   , 60 )        ;
        Integer writeTimeOut   = Utils.setDefault( _writeTimeOut  , 60 )        ;
        String  mediaType      = Utils.setDefault( _mediaType , "text/rdf+n3" ) ;
        
        DataLoader.load( url            ,
                         fileOrDir      ,
                         mediaType      ,
                         connectTimeOut ,
                         readTimeOut    ,
                         writeTimeOut   ,
                         removeOnLoad   ,
                         verbose      ) ;
    }    
 }
