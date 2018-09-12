package net.sourceforge.simcpux.xml;

import net.sourceforge.simcpux.bean.App;
import net.sourceforge.simcpux.log.L;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class MyHandler extends DefaultHandler{
    private ArrayList<App> appList = new ArrayList<>();
    private String tagName;
    private App app;
    private static final String TAG = "MyHandler";

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        tagName = localName;
        if(localName.equals("app")){
            app = new App();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        tagName = null;
        if(localName.equals("app")){
            appList.add(app);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(tagName == null){
            return;
        }
        if(tagName.equals("id")){
            app.setId(new String(ch,start,length));
        }else if(tagName.equals("name")){
            app.setName(new String(ch,start,length));
        }else if(tagName.equals("version")){
            app.setVersion(new String(ch,start,length));
        }
    }

    public ArrayList<App> getAppList() {
        return appList;
    }
}
