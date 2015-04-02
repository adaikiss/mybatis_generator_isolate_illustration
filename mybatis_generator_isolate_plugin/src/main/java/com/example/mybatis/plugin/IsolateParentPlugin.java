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
 * isolate user code from mybatis generated code through parent classes.
 * @author hlw
 * 
 */
public class IsolateParentPlugin extends PluginAdapter {

    enum IsolateType{
        None, All, Client, Model;
        static{
            All.isolateModel = true;
            All.isolateClient = true;
            Client.isolateClient = true;
            Model.isolateModel = true;
        }
        private boolean isolateModel = false;
        private boolean isolateClient = false;
        public boolean isIsolateModel(){
            return isolateModel;
        }

        public boolean isIsolateClient(){
            return isolateClient;
        }

        public static IsolateType fromText(String text){
            if(text == null || text.trim().equals("")){
                return None;
            }
            if("true".equalsIgnoreCase(text) || "all".equalsIgnoreCase(text)){
                return All;
            }
            if("false".equalsIgnoreCase(text) || "none".equalsIgnoreCase(text)){
                return None;
            }
            if("client".equalsIgnoreCase(text)){
                return Client;
            }
            if("model".equalsIgnoreCase(text)){
                return Model;
            }
            throw new RuntimeException("Unknown isolate type:" + text);
        }
    }
	private String prefix;
	private String suffix;
	private String modelPackageToReplace;
	private String modelPackageReplace;
	private String clientPackageReplace;
	private String clientPackageToReplace;
	private IsolateType defaultIsolate;
	private IsolateType isolate = IsolateType.None;
	private boolean hasPrimaryKeyClass;

	private String exampleRootClassName;
	private String clientRootClassName;

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
		defaultIsolate = IsolateType.fromText(properties.getProperty("defaultIsolate"));
		exampleRootClassName = properties.getProperty("exampleRootClass");
        clientRootClassName = properties.getProperty("clientRootInterface");
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		this.isolate = IsolateType.None;
		String isolateStr = introspectedTable.getTableConfiguration().getProperty("isolate");
		if (null != isolateStr && isolateStr.trim().length() > 0) {
			isolate = IsolateType.fromText(isolateStr);
		}else{
			isolate = defaultIsolate;
		}
		hasPrimaryKeyClass = introspectedTable.getPrimaryKeyColumns().size() > 1 && introspectedTable.getContext().getDefaultModelType() != ModelType.FLAT;
		super.initialized(introspectedTable);
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate.isIsolateModel() && !hasPrimaryKeyClass) {
			addModelIsolateParent(topLevelClass, introspectedTable);
		}
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (isolate.isIsolateModel()) {
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
		if (isolate.isIsolateClient()) {
			FullyQualifiedJavaType clientSuperClass = new FullyQualifiedJavaType(makeClientParentClass(interfaze.getType()));
			interfaze.addSuperInterface(clientSuperClass);
			if(clientPackageToReplace != null && !clientSuperClass.getShortName().equals(interfaze.getType().getShortName())){
				interfaze.addImportedType(clientSuperClass);
			}
		}
		if(clientRootClassName != null){
			FullyQualifiedJavaType mapperRootClass = new FullyQualifiedJavaType(clientRootClassName);
			mapperRootClass.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
			interfaze.addSuperInterface(mapperRootClass);
			interfaze.addImportedType(mapperRootClass);
		}
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}

	protected void addModelIsolateParent(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(makeModelParentClass(topLevelClass.getType()));
		topLevelClass.setSuperClass(superClass);
		if(modelPackageToReplace != null && !topLevelClass.getType().getShortName().equals(superClass.getShortName())){
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
