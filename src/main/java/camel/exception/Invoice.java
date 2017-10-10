package camel.exception;

import java.io.Serializable;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord( separator = "," , skipFirstLine = true )
public class Invoice implements Serializable {

	private static final long serialVersionUID = -7011650564923121597L;

	@DataField(pos = 1)
	public String ref;

	@DataField(pos = 2)
	public String email;

	@Override
	public String toString() {
		return "Invoice [ref=" + ref + ", email=" + email + "]";
	}


}