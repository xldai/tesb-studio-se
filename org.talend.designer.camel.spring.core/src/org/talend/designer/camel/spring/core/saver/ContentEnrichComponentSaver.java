package org.talend.designer.camel.spring.core.saver;

import java.util.Map;

import org.talend.designer.camel.spring.core.models.SpringRouteNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ContentEnrichComponentSaver extends AbstractComponentSaver {

	private String ID = "enrich";
	private int index = 0;
	
	public ContentEnrichComponentSaver(Document document, Element rootElement, Element contextElement) {
		super(document, rootElement, contextElement);
	}

	@Override
	public Element save(SpringRouteNode srn, Element parent) {
		Map<String, String> parameter = srn.getParameter();
		String mergeData = parameter.get(ER_MERGE_DATA);
		
		String elementName = ENRICH_ELE;
		if(ER_CONSUMER.equals(mergeData)){
			elementName = POLLENRICH_ELE;
		}
		
		Element element = document.createElement(elementName);
		parent.appendChild(element);
		
		String uri = parameter.get(ER_RESOUCE_URI);
		if(null!=uri){
			element.setAttribute("uri", uri);
		}
		
		String aggregateStrategy = parameter.get(ER_AGGREGATE_STRATEGY);
		if(null!=aggregateStrategy){
			index ++;
			Element beanElement = document.createElement(BEAN_ELE);
			root.insertBefore(beanElement, context);
			beanElement.setAttribute("id", ID+index);
			beanElement.setAttribute("class", aggregateStrategy);
			element.setAttribute("strategyRef", ID+index);
		}
		
		if(ER_CONSUMER.equals(mergeData)){
			String timeoutStyle = parameter.get(ER_TIMEOUT_STYLE);
			String timeoutValue = "0";
			if(ER_WAIT_UNTIL.equals(timeoutStyle)){
				timeoutValue = "-1";
			}else if(ER_POLL_IMMED.equals(timeoutStyle)){
				timeoutValue = "0";
			}else{
				timeoutValue = parameter.get(ER_WAIT_TIMEOUT);
			}
			element.setAttribute("timeout", timeoutValue);
		}
		
		return element;
	}

}
