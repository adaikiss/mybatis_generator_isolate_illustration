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
	private boolean isolate = false;
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
		this.isolate = false;
		String isolateStr = introspectedTable.getTableConfiguration().getProperty("isolate");
		if (null != isolateStr) {
			isolate = Boolean.valueOf(isolateStr);
		}
		hasPrimaryKeyClass = introspectedTable.getPrimaryKeyColumns().size() > 1;
		super.initialized(introspectedTable);
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate && !hasPrimaryKeyClass) {
			addIsolateParent(topLevelClass, introspectedTable);
		}
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate) {
			addIsolateParent(topLevelClass, introspectedTable);
		}
		return super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate) {
			interfaze.addSuperInterface(new FullyQualifiedJavaType(makeParentClass(interfaze.getType())));
		}
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	protected void addIsolateParent(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.setSuperClass(makeParentClass(topLevelClass.getType()));
	}

	protected String makeParentClass(FullyQualifiedJavaType type) {
		StringBuilder sb = new StringBuilder();
		return sb.append(type.getPackageName()).append(".").append(prefix).append(type.getShortName()).toString();
	}
}
