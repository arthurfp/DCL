package dclsuite.resolution;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;

import dclsuite.core.Architecture;
import dclsuite.dependencies.DeclareReturnDependency;
import dclsuite.dependencies.Dependency;
import dclsuite.enums.ConstraintType;
import dclsuite.enums.DependencyType;
import dclsuite.util.DCLUtil;

public final class AuxiliaryFunctions {

	/**
	 * Auxiliary Function: "can" We actually forward to the function "can", as
	 * implemented by the checking tool
	 */
	public static boolean can(final String classNameA, final DependencyType dependencyType, final String classNameB,
			final Architecture architecture, final IProject project) throws CoreException {
		return architecture.can(classNameA, classNameB, dependencyType, project);
	}

	/**
	 * Auxiliary function: "super(t)"
	 */
	public static Collection<String> supertypes(final String originClassName, final IProject project, final Architecture architecture,
			final String classNameToFindSuperTypes, final DependencyType dependencyType) throws CoreException {
		Collection<String> result = new LinkedList<String>();

		IJavaProject javaProject = JavaCore.create(project);
		IType type = javaProject.findType(classNameToFindSuperTypes);

		ITypeHierarchy typeHierarchy = type.newTypeHierarchy(null);
		IType[] typeSuperclasses = typeHierarchy.getAllSupertypes(type);

		for (IType t : typeSuperclasses) {
			result.add(t.getFullyQualifiedName());
		}

		return result;
	}

	/**
	 * Auxiliary function: "super(t)" We also return only the super types that
	 * origin class is allowed to <dep>.
	 */
	public static Collection<String> supertypesAllowedTo(final String originClassName, final IProject project, final Architecture architecture,
			final String classNameToFindSuperTypes, final DependencyType dependencyType) throws CoreException {
		Collection<String> result = new LinkedList<String>();

		IJavaProject javaProject = JavaCore.create(project);
		IType type = javaProject.findType(classNameToFindSuperTypes);

		ITypeHierarchy typeHierarchy = type.newTypeHierarchy(null);
		IType[] typeSuperclasses = typeHierarchy.getAllSupertypes(type);

		for (IType t : typeSuperclasses) {
			if (architecture.can(originClassName, t.getFullyQualifiedName(), dependencyType, project)) {
				result.add(t.getFullyQualifiedName());
			}
		}

		return result;
	}

	/**
	 * Auxiliary function: "sub(t)"
	 */
	public static Collection<String> subtypes(final String originClassName, final IProject project, final Architecture architecture,
			final String classNameToFindSubTypes, final DependencyType dependencyType) throws CoreException {
		Collection<String> result = new LinkedList<String>();

		IJavaProject javaProject = JavaCore.create(project);
		IType type = javaProject.findType(classNameToFindSubTypes);

		ITypeHierarchy typeHierarchy = type.newTypeHierarchy(null);
		IType[] typeSubclasses = typeHierarchy.getAllSubtypes(type);

		for (IType t : typeSubclasses) {
			result.add(t.getFullyQualifiedName());
		}

		return result;
	}

	/**
	 * Auxiliary function: "sub(t)" We also return only the sub types that
	 * origin class is allowed to <dep>.
	 */
	public static Collection<String> subtypesAllowedTo(final String originClassName, final IProject project, final Architecture architecture,
			final String classNameToFindSubTypes, final DependencyType dependencyType) throws CoreException {
		Collection<String> result = new LinkedList<String>();

		IJavaProject javaProject = JavaCore.create(project);
		IType type = javaProject.findType(classNameToFindSubTypes);

		ITypeHierarchy typeHierarchy = type.newTypeHierarchy(null);
		IType[] typeSubclasses = typeHierarchy.getAllSubtypes(type);

		for (IType t : typeSubclasses) {
			if (architecture.can(originClassName, t.getFullyQualifiedName(), dependencyType, project)) {
				result.add(t.getFullyQualifiedName());
			}
		}

		return result;
	}

	/**
	 * Function to find a factory
	 */
	public static String[] factory(final IProject project, final Architecture architecture, final String classNameToFindFactory) {
		for (String className : architecture.getProjectClasses()) {
			Collection<Dependency> dependencies = architecture.getDependencies(className);

			for (Dependency d : dependencies) {
				if (d instanceof DeclareReturnDependency) {
					if (d.getClassNameB().equals(classNameToFindFactory)) {
						DeclareReturnDependency drd = (DeclareReturnDependency) d;
						return new String[] { className, drd.getMethodName() };
					}
				}
			}
		}
		return null;

	}

	public static boolean isModuleMequalModuleMa(final String className, final String moduleDescription,
			final Set<ModuleSimilarity> suitableModules, final Map<String, String> modules, final Collection<String> projectClassNames,
			final IProject project, final ConstraintType constraintType) {

		String suitableModulesDescription = "";

		if (suitableModules != null && !suitableModules.isEmpty()) {
			for (ModuleSimilarity ms : suitableModules) {
				suitableModulesDescription += ms.getModuleDescription() + ",";
			}
			suitableModulesDescription = suitableModulesDescription.substring(0, suitableModulesDescription.length() - 1);
		}

		final String simpleClassName = DCLUtil.getSimpleClassName(className);

		for (ModuleSimilarity m : suitableModules) {
			if (m.getModuleDescription().endsWith(".*")) {
				/* Lets simulate if it had been moved */
				String qualifiedClassName = m.getModuleDescription().replaceAll("\\.\\*", "") + "." + simpleClassName;

				if (DCLUtil.hasClassNameByDescription(qualifiedClassName, moduleDescription, modules, projectClassNames, project)) {
					return (constraintType == ConstraintType.CANNOT || constraintType == ConstraintType.CAN_ONLY) ? true : false;
				}
			} else if (moduleDescription.contains(m.getModuleDescription())) {
				/* It's the same as the description */
				return (constraintType == ConstraintType.CANNOT || constraintType == ConstraintType.CAN_ONLY) ? true : false;
			}

		}

		return false;
	}

}
