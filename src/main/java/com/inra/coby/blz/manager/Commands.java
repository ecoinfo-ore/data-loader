
package com.inra.coby.blz.manager;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ryahiaoui
 */
public class Commands {
    
    public static boolean mkdir(String directory ) {
      File fDirectory = new File(directory) ;
      if( ! fDirectory.exists() )   {
          System.out.println(" Create Folder : " + directory ) ;
          return fDirectory.mkdir() ;
      }
      return false ; 
    }
    
    public static void rm(String path ) throws IOException {
      System.out.println(" Remove Path : " + path ) ;
      if(new File(path).isDirectory()) {
        FileUtils.deleteDirectory(new File(path))   ;
      }
      else {
        new File(path).delete() ;
      }
    }
       
}
