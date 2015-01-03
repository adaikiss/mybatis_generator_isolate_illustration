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
import org.mybatis.generator.config.ModelType;

/**
 * @author hlw
 * 
 */
public class IsolateParentPlugin extends PluginAdapter {

	private String prefix;
	private String suffix;
	private String modelPackageToReplace;
	private String modelPackageReplace;
	private String clientPackageReplace;
	private String clientPackageToReplace;
	private boolean defaultIsolate;
	private boolean isolate = false;
	private boolean hasPrimaryKeyClass;

	private String exampleRootClassName;
	private String mapperRootClassName;

	public IsolateParentPlugin() {
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		prefix = properties.getProperty("isolatePrefix", null);
		suffix = properties.getProperty("isolateSuffix", null);
		String modelPackageReplaceStr = properties.getProperty("modelParentReplacePackage", null);
		if(modelPackageReplaceStr != null){
			String[] split = modelPackageReplaceStr.split("\\|");
			this.modelPackageToReplace = split[0];
			this.modelPackageReplace = split[1];
		}
		String clientPackageReplaceStr = properties.getProperty("clientParentReplacePackage", null);
		if(clientPackageReplaceStr != null){
			String[] split = clientPackageReplaceStr.split("\\|");
			this.clientPackageToReplace = split[0];
			this.clientPackageReplace = split[1];
		}
		defaultIsolate = Boolean.valueOf(properties.getProperty("defaultIsolate", "false"));
		exampleRootClassName = properties.getProperty("exampleRootClass");
		mapperRootClassName = properties.getProperty("mapperRootInterface");
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		this.isolate = false;
		String isolateStr = introspectedTable.getTableConfiguration().getProperty("isolate");
		if (null != isolateStr && isolateStr.trim().length() > 0) {
			isolate = Boolean.valueOf(isolateStr);
		}else{
			isolate = defaultIsolate;
		}
		hasPrimaryKeyClass = introspectedTable.getPrimaryKeyColumns().size() > 1 && introspectedTable.getContext().getDefaultModelType() != ModelType.FLAT;
		super.initialized(introspectedTable);
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate && !hasPrimaryKeyClass) {
			addModelIsolateParent(topLevelClass, introspectedTable);
		}
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate) {
			addModelIsolateParent(topLevelClass, introspectedTable);
		}
		return super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if(exampleRootClassName != null){
			addExampleRootClass(topLevelClass, introspectedTable);
		}
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate) {
			FullyQualifiedJavaType clientSuperClass = new FullyQualifiedJavaType(makeClientParentClass(interfaze.getType()));
			interfaze.addSuperInterface(clientSuperClass);
			if(clientPackageToReplace != null){
				interfaze.addImportedType(clientSuperClass);
			}
		}
		if(mapperRootClassName != null){
			FullyQualifiedJavaType mapperRootClass = new FullyQualifiedJavaType(mapperRootClassName);
			mapperRootClass.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
			interfaze.addSuperInterface(mapperRootClass);
			interfaze.addImportedType(mapperRootClass);
		}
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	protected void addModelIsolateParent(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String superClass = makeModelParentClass(topLevelClass.getType());
		topLevelClass.setSuperClass(superClass);
		if(modelPackageToReplace != null){
			topLevelClass.addImportedType(superClass);
		}
	}

	protected String makeModelParentClass(FullyQualifiedJavaType type) {
		StringBuilder sb = new StringBuilder();
		if(modelPackageToReplace != null){
			sb.append(type.getPackageName().replace(modelPackageToReplace, modelPackageReplace));
		}else{
			sb.append(type.getPackageName());
		}
		sb.append(".");
		if(prefix != null){
			sb.append(prefix);
		}
		sb.append(type.getShortName());
		if(suffix != null){
			sb.append(suffix);
		}
		return sb.toString();
	}
	protected String makeClientParentClass(FullyQualifiedJavaType type) {
		StringBuilder sb = new StringBuilder();
		if(clientPackageToReplace != null){
			sb.append(type.getPackageName().replace(clientPackageToReplace, clientPackageReplace));
		}else{
			sb.append(type.getPackageName());
		}
		sb.append(".");
		if(prefix != null){
			sb.append(prefix);
		}
		sb.append(type.getShortName());
		if(suffix != null){
			sb.append(suffix);
		}
		return sb.toString();
	}

	protected void addExampleRootClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType exampleRootClass = new FullyQualifiedJavaType(exampleRootClassName);
		exampleRootClass.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
		topLevelClass.setSuperClass(exampleRootClass);
		topLevelClass.addImportedType(exampleRootClass);
	}
}
