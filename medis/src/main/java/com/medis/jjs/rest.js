function rest() {
	return {
		/**
		 * Handles HTTP JSON requests/responses only
		 * 
		 * url - http url with or without query components
		 * queryParams - optional - if separate query components are passed
		 * method - the type of http request
		 * request - the http payload body 
		 */
		http:function(url, queryParams, method, request) {
			method = (method == null ? "GET" : method);
			if (queryParams != undefined ) {
					var queryDelimiter = "?";
					for (var key in queryParams ) {
						if (queryParams.hasOwnProperty(key)) {
							url = url + queryDelimiter + key + "=" + queryParams[key];
							queryDelimiter = "&";
						}
					}
			}
			
			with( new JavaImporter(java.io,java.net)) {
				print("/" + method + " " + url);
				var conn = new URL(url).openConnection();
				var reader;
				try {
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestMethod(method);
					conn.setDoInput(true);
					conn.setDoOutput(true);
					
					if (request) {
						print("requestBody=" + JSON.stringify(request));
						//Send body
						var wr = new DataOutputStream(conn.getOutputStream ());
						wr.writeBytes(request);
						wr.flush ();
						wr.close ();
					}
					
					var reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					var buf = '', line = null;
					while((line = reader.readLine()) != null) {
						buf += line;
					}
				} finally {
					if (reader)
						reader.close();
					if (conn)
						conn.disconnect();
				}
				
				return buf;
			}
		},
		
		get:function( url, request ) {
			return JSON.parse(this.http(url,null,"GET",request));
		},
		
		getAsString:function( url, request ) {
			return (this.http(url,null,"GET",request) );
		},
		
		post:function(url, request ) {
			return JSON.parse(this.http(url,null,"POST",request));
		},
		
		postAsString:function( url, request ) {
			return (this.http(url,null,"POST",request) );
		},
		
		execFile:function(jsonFile) {
			var data = readFully(jsonFile);
			var jsonData = JSON.parse(data);
			var resp;
			if (jsonData.method === "GET") {
				resp = this.get(jsonData.url, jsonData.body);
			} else if (jsonData.method === "POST") {
				resp = this.post(jsonData.url, jsonData.body);
			} else {
				resp = this.http(jsonData.url, null, jsonData.method, jsonData.body);
				resp = JSON.parse(resp);
			}
			
			return resp;
		}

	};
}

var REST = rest();