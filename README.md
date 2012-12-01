GenericType -  Runtime Generic Type Resolution for Java
================================================= 

![Logo][logo]

GenericType is a utility class that considerably eases the resolution of generic type information at runtime.  

Author: Michael Karneim

Project Homepage: http://github.com/mkarneim/generictype

About
-----

Given you have an interface that extends the generic interface Collection<E>:

	public interface MyCollection extends Collection<String> {
	}

You can see that the value of the generic type parameter is ```String```.

But do you know how to access this value using reflection?
Actually it's possible because the value is compiled into the class file of ```MyCollection```.

But if you try to write some code that can resolve it, you'll possibly realize that this not a simple task.
I stumbled upon this problem during my work on [Beanfabrics]. 
And it took me quite a while to write some code that can do it.
 
Finally I created this repository with my solution called ```GenericType```.

So how can you use it?
-----

First choose the type variable you are interested by using standard reflection: 
	
	TypeVariable E = Collection.class.getTypeParameters()[0];
	
Then create an instance of ```GenericType``` with a reference to ```MyCollection```.

	GenericType classType = new GenericType(MyCollection.class);
	
And now you can access the actual value of the type parameter:

	GenericType typeParam = classType.getTypeParameter(E);
	
	Assert.assertEquals( String.class, typeParam.asClass());

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
[Beanfabrics]: http://beanfabrics.org
[logo]: http://github.com/mkarneim/generictype/blob/master/logo.png "GenericType"
