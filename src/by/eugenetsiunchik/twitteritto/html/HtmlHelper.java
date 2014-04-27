package by.eugenetsiunchik.twitteritto.html;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.util.Log;

public class HtmlHelper {
	  private static final String TAG = HtmlHelper.class.getSimpleName();
	TagNode rootNode;

	  //Конструктор
	  public HtmlHelper(URL htmlPage) throws IOException
	  {
	    //Создаём объект HtmlCleaner
	    HtmlCleaner cleaner = new HtmlCleaner();
	    //Загружаем html код сайта
	    rootNode = cleaner.clean(htmlPage);
	  }

	  List<String> getImgLinks()
	  {
	    List<String> linkList = new ArrayList<String>();
	    
	    
	    //Выбираем все ссылки
	    TagNode linkElements[] = rootNode.getElementsByName("img", true);
	    for (int i = 0; linkElements != null && i < linkElements.length; i++)
	    {
	    	Log.i(TAG, linkElements[i].getName());
	    	        String src = linkElements[i].getAttributeByName("src");
	                if (src != null) {
	                    linkList.add(src);
	                    Log.i(TAG,"src " + src);
	                
	            }
	    }

	    return linkList;
	  }
	}
