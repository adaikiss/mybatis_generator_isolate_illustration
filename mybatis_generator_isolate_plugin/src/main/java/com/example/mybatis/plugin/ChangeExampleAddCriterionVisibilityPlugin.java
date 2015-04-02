/**
 * 
 */
package com.example.mybatis.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * change addCriterion's visibility to public, then we can add criterion like:<br/>
 * criteria.addCriterion("date(a.create_time) =", date);<br/>
 * @author hlw
 *
 */
public class ChangeExampleAddCriterionVisibilityPlugin extends PluginAdapter {
	private static String generatedCriteria = "GeneratedCriteria";
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		InnerClass answer = getGeneratedCriteria(topLevelClass);
		for(Method method : answer.getMethods()){
			if(method.getName().equals("addCriterion")){
				method.setVisibility(JavaVisibility.PUBLIC);
			}
		}
		return true;
	}

	private InnerClass getGeneratedCriteria(TopLevelClass topLevelClass){
		for(InnerClass inner : topLevelClass.getInnerClasses()){
			if(inner.getType().getShortName().equals(generatedCriteria)){
				return inner;
			}
		}
		return null;
	}
}
