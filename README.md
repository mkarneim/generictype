GenericType -  Runtime Generic Type Resolution for Java
================================================= 

Author: Michael Karneim

Project Homepage: http://github.com/mkarneim/generictype

About
-----

GenericType is a utility class considerably eases the resolution of generic type information at runtime.  

Here is an example of how you could use GenericType from your code.

Given you have a class that extends the generic Collection interface:

	public class StringCollection extends Collection<String> {
	}
		
When you create a GenericType object like this:		

	GenericType classType = new GenericType(StringCollection.class);
		
Then you can access the actual generic type parameter value like this:

	GenericType typeParam = classType.getTypeParameter(Collection.class.getTypeParameters()[0]);
				
	Assert.assertEquals("typeParam.getType()", String.class, typeParam.getType());

