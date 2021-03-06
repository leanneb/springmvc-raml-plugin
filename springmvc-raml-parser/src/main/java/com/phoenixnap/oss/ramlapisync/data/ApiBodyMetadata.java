/*
 * Copyright 2002-2016 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.phoenixnap.oss.ramlapisync.data;

import org.raml.model.ParamType;
import org.raml.parser.utils.Inflector;

import com.phoenixnap.oss.ramlapisync.naming.SchemaHelper;
import com.sun.codemodel.JCodeModel;


/**
 * 
 * Class containing the data required to successfully generate code for an api request or response body
 * 
 * @author Kurt Paris
 * @since 0.2.1
 *
 */	
public class ApiBodyMetadata {
	
	private String name;
	private String schema;
	private JCodeModel codeModel;
	private boolean array = false;
	
	public ApiBodyMetadata (String name, String schema, JCodeModel codeModel) {
		super();
		this.schema = schema;
		this.name = name;
		this.codeModel = codeModel;
		
		int typeIdx = schema.indexOf("type");
		
		if (typeIdx != -1) {
			int quoteIdx = schema.indexOf("\"", typeIdx + 6);
			if (quoteIdx != -1) {
				int nextQuoteIdxIdx = schema.indexOf("\"", quoteIdx+1);
				if (nextQuoteIdxIdx != -1) {
					String possibleType = schema.substring(quoteIdx+1, nextQuoteIdxIdx);
					if ("array".equals(possibleType.toLowerCase())) {
						array = true;
						this.name = Inflector.singularize(this.name);
					}
					if (codeModel.countArtifacts() == 0) {
						if (!"object".equals(possibleType.toLowerCase())) {
							try {
								this.name = SchemaHelper.mapSimpleType(ParamType.valueOf(possibleType.toUpperCase())).getSimpleName();
							} catch (Exception ex) {
								this.name = String.class.getSimpleName(); //default to string
							}
							this.codeModel = null;
						}
						
				}
			}
		}
		}
	}
	
	public String getName() {
		return name;
	}
	public String getSchema() {
		return schema;
	}
	public JCodeModel getCodeModel() {
		return codeModel;
	}

	public boolean isArray() {
		return array;
	}

}
