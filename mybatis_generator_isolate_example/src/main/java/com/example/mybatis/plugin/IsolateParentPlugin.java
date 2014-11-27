/**
 * 
 */
package com.example.mybatis.plugin;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * @author hlw
 * 
 */
public class IsolateParentPlugin extends PluginAdapter {

	private String prefix;
	private boolean isolate;
	private boolean hasPrimaryKeyClass;

	public IsolateParentPlugin() {

	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		prefix = properties.getProperty("isolatePrefix");
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		String isolateStr = introspectedTable.getTableConfiguration().getProperty("isolate");
		if (null != isolateStr) {
			isolate = Boolean.valueOf(isolateStr);
		}
		hasPrimaryKeyClass = introspectedTable.getPrimaryKeyColumns().size() > 1;
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate && !hasPrimaryKeyClass) {
			addIsolateParent(topLevelClass, introspectedTable);
		}
		return true;
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate) {
			addIsolateParent(topLevelClass, introspectedTable);
		}
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		interfaze.addSuperInterface(new FullyQualifiedJavaType(makeParentClass(interfaze.getClass())));
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	protected void addIsolateParent(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.setSuperClass(makeParentClass(topLevelClass.getClass()));
	}

	protected String makeParentClass(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		return sb.append(clazz.getPackage().getName()).append(".").append(prefix).append(clazz.getSimpleName())
				.toString();
	}
}
