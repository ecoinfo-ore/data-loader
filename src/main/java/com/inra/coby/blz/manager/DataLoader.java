
package com.inra.coby.blz.manager;

import okio.Okio;
import okio.Buffer;
import okio.Source;
import java.io.File;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okhttp3.MediaType;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import okhttp3.RequestBody;
import java.nio.file.Paths;
import okhttp3.OkHttpClient;
import java.util.stream.Stream;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ryahiaoui
 */

public class DataLoader {
    
    public static void load ( String  url            ,
                              String  fileOrDir      ,
                              String  mediaType      ,
                              int     connectTimeOut , 
                              int     readTimeOut    ,
                              int     writeTimeOut   ,
                              boolean removeOnLoad   ,
                              boolean verbose        ) throws Exception {
        
        MediaType Media_Type= MediaType.parse(mediaType) ;
                 
        OkHttpClient client =  new OkHttpClient.Builder()
                                               .connectTimeout( connectTimeOut , TimeUnit.SECONDS)
                                               .writeTimeout( writeTimeOut , TimeUnit.SECONDS)
                                               .readTimeout( readTimeOut , TimeUnit.SECONDS)
                                               .build() ;
       
        if( Files.isDirectory(Paths.get(fileOrDir))) { // is Dir
            
            System.out.println(" \n + Load All Files in Directory : " + fileOrDir ) ;
            
            try (Stream<Path> walk = Files.walk(Paths.get(fileOrDir))) {

		walk.filter(Files::isRegularFile)
		    .forEach( path  -> loadFile ( url           ,
                                                  Media_Type    ,
                                                  path.toFile() ,
                                                  client        , 
                                                  verbose       ,
                                                  removeOnLoad  ) ) ;

            } catch (IOException e ) {
              e.printStackTrace()  ;
           }
            
        } else  { // is File
            loadFile( url, Media_Type, new File(fileOrDir), client , verbose, removeOnLoad ) ;
        }
    }

    private static void loadFile( String url           , 
                                  MediaType Media_Type , 
                                  File file            ,
                                  OkHttpClient client  ,
                                  boolean verbose      , 
                                  boolean removeOnLoad )  {
        
        System.out.println("\n - Load File : " + file.getAbsolutePath() + "\n " ) ;
            
        Request request = new Request.Builder()
                .url(url)
                .post(createCustomRequestBody(Media_Type, file, verbose))
                .build() ;
        
        try ( Response res = client.newCall(request).execute() ) {
            if ( !res.isSuccessful()) throw new IOException("Unexpected code " + res) ;
            System.out.println( "    " + res.body().string() + "\n" ) ;
        } catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        
        if( removeOnLoad ) file.deleteOnExit() ;
    }
    
    public static RequestBody createCustomRequestBody( final MediaType contentType, final File file, boolean verbose ) {
        
        return new RequestBody() {
            
            @Override public MediaType contentType() {
                return contentType;
            }
            @Override public long contentLength()    {
                return file.length();
            }
            @Override public void writeTo(BufferedSink sink) throws IOException {

                Source source ;

                try {
                    source     = Okio.source(file)   ;
                    Buffer buf = new Buffer()        ;
                    Long remaining = contentLength() ;

                    for (long readCount; (readCount = source.read(buf, 4096)) != -1; ) {
                        sink.write(buf, readCount) ;
                        
                        if( verbose ) {
                            System.out.println( "source size: " + contentLength() +
                                                " remaining bytes: "              + 
                                                ( remaining -= readCount ) )      ;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace() ;
                }
            }
        };
   }
}
