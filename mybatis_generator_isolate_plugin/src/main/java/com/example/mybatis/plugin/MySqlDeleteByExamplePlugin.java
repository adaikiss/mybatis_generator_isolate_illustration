package com.example.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class MySqlDeleteByExamplePlugin extends PluginAdapter {

	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		for (int i = 0; i < element.getElements().size(); i++) {
			Element elem = element.getElements().get(i);
			if (elem instanceof TextElement
					&& elem.getFormattedContent(0).contains("delete from "))
				replaceDeleteStatement(element, table, i);
		}
		return true;
	}

	private void replaceDeleteStatement(XmlElement element,
			FullyQualifiedTable table, int index) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ");
		String alias = table.getAlias();
		boolean hasAlias = alias != null && alias.length() > 0;
		if (hasAlias) {
			sb.append(alias);
			sb.append(" using ");
		}
		sb.append(table.getFullyQualifiedTableNameAtRuntime());
		if(hasAlias){
			sb.append(" as ").append(alias).append(" ");
		}
		element.getElements().set(index, new TextElement(sb.toString()));
	}
}