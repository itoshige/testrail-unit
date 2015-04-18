package org.itoshige.testrail.annotation;

import java.lang.annotation.Annotation;


/**
 * @author abhijeet.burle
 *
 */
public class LinkTestRailHelper{
	
	/**
	 * This method looks for annotation <T> in the targetClass, if not found it looks up the package where the targetClass is located,
	 * if keeps on looking for the annotation in the package going one level until it finds the annotation or reaches the top package.
	 * Example if the Class example1 is in package level1.level2.level3
	 * 	it will check the annotation in class example1
	 *  if not found it will check the package level1.level2.level3
	 *  if not found it will check the package level1.level2
	 *  if not found it will check the package level1
	 *  if still not found it will return null
	 * @param targetClass
	 * @param annotationClass
	 * @return returns the annotation if found in precedence mentioned in the description. If none of them have the annotation then it returns null. 
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> targetClass, Class<T> annotationClass){
		return getAnnotation(targetClass, annotationClass, true);
	}
	
	/**
	 * @param targetClass
	 * @param annotationClass
	 * @param isPackageInheritanceEnabled
	 * @return
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> targetClass, Class<T> annotationClass, boolean isPackageInheritanceEnabled){
		T targetedAnnotation = null;
		
		targetedAnnotation = targetClass.getAnnotation(annotationClass);
		// If not found at class level look for package level annotation
		if(targetedAnnotation==null && targetClass.getPackage()!=null){
			targetedAnnotation = targetClass.getPackage().getAnnotation(annotationClass);
			if(targetedAnnotation==null){
				// Not found at current package level
				// Let try a hack and start going up the package to find it on top level packages
				
				Package targetPackage = null;
				// Get the package name
				String packageName = targetClass.getPackage().getName();
				int numberOfLevels = packageName.length() - packageName.replace(".", "").length();
				
				// Go one level up until the annotation is found or there is no more level to go 
				for (int i = numberOfLevels; (i > 0 && targetedAnnotation==null); --i) {
					// Get the package name one level up
					int end = packageName.lastIndexOf(".");
					if(end!=-1)
						packageName=packageName.substring(0, end);
					
					try {
					    Class.forName(packageName+".package-info");
					} catch(Exception e) {
						// I know it is bad to just swallow an exception, but out here i have no other choice
					}
					targetPackage = Package.getPackage(packageName);
					if(targetPackage!=null){
						targetedAnnotation = targetPackage.getAnnotation(annotationClass);
					}
				}
				// Nothing found. So sadly have to return NULL. 
				// Or should i throw an exception instead?
			}
		}
    	
		return targetedAnnotation;
    }
}
