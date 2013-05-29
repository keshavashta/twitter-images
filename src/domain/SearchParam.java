package domain;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: saxena.arunesh
 * Date: 5/25/13
 * Time: 6:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchParam {


    private String query;
    private int pageNumber;
    private int limit;
    private ArrayList<NameValuePair> params;

    public SearchParam(String query, int pageNumber, int limit) {
        this.query = query;
        this.pageNumber = pageNumber;
        this.limit = limit;
        this.params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("q", query));
        params.add(new BasicNameValuePair("page", "" + pageNumber));
        params.add(new BasicNameValuePair("limit", "" + limit));
        params.add(new BasicNameValuePair("replies", "0"));
        params.add(new BasicNameValuePair("include_entities", "1"));
        params.add(new BasicNameValuePair("retweets", "1"));
        params.add(new BasicNameValuePair("rpp", "1000"));
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        params.remove(1);
        params.add(new BasicNameValuePair("page", "" + pageNumber));
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ArrayList<NameValuePair> getParams() {
        return params;
    }

    public void setParams(ArrayList<NameValuePair> params) {
        this.params = params;
    }
}
