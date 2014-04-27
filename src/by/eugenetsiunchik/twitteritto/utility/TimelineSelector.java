package by.eugenetsiunchik.twitteritto.utility;

public class TimelineSelector extends Object {
	public String url; // the url to perform the query from
	// not all these apply to every url - you are responsible
	public String since_id; // ids newer than this will be fetched
	public String max_id; // ids older than this will be fetched
	public Integer count; // # of tweets to fetch Max is 200
	public Integer page; // # of page to fetch (with limits)

	public TimelineSelector(String u) {
		url = u;
		max_id = null;
		since_id = null;
		count = null;
		page = null;
	}
	public TimelineSelector(String u, String since) {
		url = u;
		max_id = null;
		since_id = since;
		count = null;
		page = null;
	}
	

	public TimelineSelector(String u, String since, String max, Integer cnt,
			Integer pg) {
		url = u;
		max_id = max;
		since_id = since;
		count = cnt;
		page = pg;
	}
}
