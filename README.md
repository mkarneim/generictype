GenericType -  Runtime Generic Type Resolution for Java
================================================= 

Author: Michael Karneim

Project Homepage: http://github.com/mkarneim/generictype

About
-----

GenericType is a utility class considerably eases the resolution of generic type information at runtime.  


Here is an example of how you could use GenericType from your code.

Given you have an interface that extends the generic Collection<E> interface:

	public interface StringCollection extends Collection<String> {
	}
		
When you create a GenericType object like this:		

	GenericType classType = new GenericType(StringCollection.class);
		
Then you can access the actual generic type parameter value like this:

	GenericType typeParam = classType.getTypeParameter(Collection.class.getTypeParameters()[0]);
				
	Assert.assertEquals("typeParam.getType()", String.class, typeParam.getType());

Download
--------

For downloading a binary distribution please visit the [download page].

License
-------

GenericType is open source, and it is distributed under the terms of the [LGPL] license. 
Please read the [license.txt].

Dependencies
------------

* [Java] 6 

Examples
--------
For some examples please have a look at the [unit tests].


[download page]: http://github.com/mkarneim/generictype/archives/master
[license.txt]: http://github.com/mkarneim/generictype/blob/master/license.txt
[LGPL]: http://github.com/mkarneim/generictype/blob/master/lgpl.txt
[Java]: http://www.oracle.com/technetwork/java/
[unit tests]: http://github.com/mkarneim/generictype/blob/master/src/test/java/org/codefabrics/generictype/GenericTypeTest.java

