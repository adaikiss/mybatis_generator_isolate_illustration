/**
 * 
 */
package com.example.mybatis.plugin;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * add example root class
 * @author hlw
 * 
 */
public class ExampleRootClassPlugin extends PluginAdapter {

	private String rootClassName;
	private FullyQualifiedJavaType rootClass;

	public ExampleRootClassPlugin() {
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		rootClassName = properties.getProperty("rootClass");
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		rootClass = new FullyQualifiedJavaType(rootClassName);
		super.initialized(introspectedTable);
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		rootClass.addTypeArgument(topLevelClass.getType());
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		addRootClass(topLevelClass, introspectedTable);
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	protected void addRootClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.setSuperClass(rootClass);
		topLevelClass.addImportedType(rootClass);
	}
}
