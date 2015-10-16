// This file was generated by Mendix Business Modeler.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package java_library.actions;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import system.proxies.FileDocument;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;

/**
 * Do an HTTP Post to the give URL.
 * The data is passed as the body and any URL can be used to call'
 * 
 * The response of the POST is written in a new system.FileDocument
 */
public class Call_URL extends CustomJavaAction<IMendixObject>
{
	private String Data;
	private String URLString;

	public Call_URL(IContext context, String Data, String URLString)
	{
		super(context);
		this.Data = Data;
		this.URLString = URLString;
	}

	@Override
	public IMendixObject executeAction() throws Exception
	{
		// BEGIN USER CODE
		// Create a URL connection
		URL url = new URL(this.URLString);
		HttpURLConnection conn = null;

			conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches (false);

		Core.getLogger(this.toString()).debug("Succesfully connected to url: " + this.URLString);
		// Write the data to the connection.
		OutputStreamWriter Writer = null;
		if(this.Data != null)
		{
			Core.getLogger(this.toString()).debug("Sending: " + this.Data);

			// Send POST output.
			DataOutputStream printout = new DataOutputStream( conn.getOutputStream() );
			printout.writeBytes (this.Data);
			printout.flush ();
			printout.close ();

			//			Writer = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
			//			Writer.write(this.Data);
			//			Writer.flush();
		}
		int status = (conn).getResponseCode();
		Core.getLogger(this.toString()).debug("Response code: " + status );
		for (Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
			Core.getLogger(this.toString()).debug(header.getKey() + "=" + header.getValue());
		}


		// Get the response and save the answer of the connection to a filedocument object.
		IMendixObject document = Core.instantiate(this.getContext(), FileDocument.entityName );
		InputStream instream = conn.getInputStream();
		if(instream.available() == 0)
		{
			Core.getLogger(this.toString()).debug("The inputstreams from the connection haves zero bytes in the result.");
		}
		Core.storeFileDocumentContent(this.getContext(), document, instream);
		Core.getLogger(this.toString()).debug("Stored the result in a filedocument");

		// Close connections.
		conn.disconnect();
		if(Writer != null)
			Writer.close();


		// Return saved object.
		return document;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@Override
	public String toString()
	{
		return "Call_URL";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}