package dclsuite.tests;

import java.util.List;

import dclsuite.core.DependencyConstraint.ArchitecturalDrift;
import dclsuite.core.DependencyConstraint.DivergenceArchitecturalDrift;
//import dclsuite.dependencies./*Missing implementation*/;

/**
 * An example of a DCLTestCase 
 * 1. Test classes must end with "TestCase" and extend DCLTestCase
 * 2. Test methods must start with "test" followed by a two digit number, e.g., test01(), test02(), etc.
 * 3. There is no attributes or constructors, i.e., only methods.
 * 4. There is pre-defined modules: MA, MB, MC, and MEX;
 *    MA  refers to classes of com.example.a.*
 *    MB  refers to classes of com.example.b.*
 *    MC  refers to classes of com.example.c.*
 *    MEX refers to classes of com.example.ex.*
 * @author Ricardo Terra
 */
public class A120TestCase extends DCLTestCase { 

	public void test01() throws Exception {
		List<ArchitecturalDrift> violations = this.validateSystem("com.example.a.A119 cannot-declare com.example.b.B119"); //Define the constraint to be validated
		
		assertEquals(0, violations.size()); //Check the number of violations (usually only one violation for constraint)
		//ArchitecturalDrift ad = violations.get(0);
		
		//assertEquals(DivergenceArchitecturalDrift.class, ad.getClass()); //Check the type of violation (divergence or absence)
		//DivergenceArchitecturalDrift dad = (DivergenceArchitecturalDrift) ad;
		
		//assertEquals(Declare[]Dependency.class, dad.getForbiddenDependency().getClass()); //Check the type of dependency
		//Declare[]Dependency declare[]Dependency = (Declare[]Dependency) dad.getForbiddenDependency();

		
		//Check each attribute of the violation
		//assertEquals("com.example.a.A120",declare[]Dependency.getClassNameA());
		//assertEquals("com.example.c.C120",declare[]Dependency.getClassNameB());
		
		//assertEquals("f",declare[]Dependency.getMethodName());
	}

}


/*
 * [!!!] Test changed to accept violations.size = 0. [!!!]
 */
