/*
 * GenericType - Runtime Generic Type Resolution for Java 
 * (C) by Michael Karneim, codefabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.codefabrics.generictype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileFilter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JDialog;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public class GenericTypeTest {
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(GenericTypeTest.class);
	}

	@Test
	public void testCollection() {
		// Given:
		Class<?> cls = Collection.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam.isTypeVariable()", typeParam.isTypeVariable());
	}

	private static interface StringCollection extends Collection<String> {

	}

	@Test
	public void testStringCollection() {
		// Given:
		Class<?> cls = StringCollection.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", String.class, typeParam.getType());
	}

	private static abstract class StringCollectionClass implements Collection<String> {

	}

	@Test
	public void testStringCollectionClass() {
		// Given:
		Class<?> cls = StringCollectionClass.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", String.class, typeParam.getType());
	}

	private static class NoGenericClass extends Object {

	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoGenericClass() {
		// Given:
		Class<?> cls = NoGenericClass.class;
		GenericType gt = new GenericType(cls);
		// When:
		gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then: this must throw an IllegalArgumentException
	}

	private static interface IntegerStringMap extends Map<Integer, String> {
	}

	@Test
	public void testIntegerStringMap() {
		// Given:
		Class<?> cls = IntegerStringMap.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam1 = gt.getTypeParameter(Map.class.getTypeParameters()[0]);
		GenericType typeParam2 = gt.getTypeParameter(Map.class.getTypeParameters()[1]);
		// Then:
		assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
		assertEquals("typeParam2.getType()", String.class, typeParam2.getType());
	}

	private static interface StringIntegerMap extends Map<String, Integer> {
	}

	@Test
	public void testStringIntegerMap() {
		// Given:
		Class<?> cls = StringIntegerMap.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam1 = gt.getTypeParameter(Map.class.getTypeParameters()[0]);
		GenericType typeParam2 = gt.getTypeParameter(Map.class.getTypeParameters()[1]);
		// Then:
		assertEquals("typeParam1.getType()", String.class, typeParam1.getType());
		assertEquals("typeParam2.getType()", Integer.class, typeParam2.getType());
	}

	private static interface SwitchArgumentsMap<VE, KE> extends Map<KE, VE> {
	}

	private static interface AnotherIntegerStringMap extends SwitchArgumentsMap<String, Integer> {
	}

	@Test
	public void testAnotherIntegerStringMap() {
		// Given:
		Class<?> cls = AnotherIntegerStringMap.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam1 = gt.getTypeParameter(Map.class.getTypeParameters()[0]);
		GenericType typeParam2 = gt.getTypeParameter(Map.class.getTypeParameters()[1]);
		// Then:
		assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
		assertEquals("typeParam2.getType()", String.class, typeParam2.getType());
	}

	private static interface SomeCollection<T> extends Collection<T> {

	}

	@Test
	public void testSomeCollection() {
		// Given:
		Class<?> cls = SomeCollection.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam.isTypeVariable()", typeParam.isTypeVariable());
	}

	private static abstract class ThrowableCollectionClass implements SomeCollection<Throwable> {

	}

	@Test
	public void testThrowableCollectionClass() {
		// Given:
		Class<?> cls = ThrowableCollectionClass.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", Throwable.class, typeParam.getType());
	}

	@SuppressWarnings("serial")
	private static class StringList extends ArrayList<String> {

	}

	@Test
	public void testStringList() {
		// Given:
		Class<?> cls = StringList.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", String.class, typeParam.getType());
	}

	private static class Outer<E extends Number> {
		@SuppressWarnings("serial")
		private class Inner extends ArrayList<E> {

		}

		Inner inner;

		@SuppressWarnings("unused")
		public Inner getInner() {
			return inner;
		}
	}

	private static class Subclass extends Outer<Integer> {

	}

	@Test
	public void testSubclassField() {
		// Given:
		Class<?> cls = Subclass.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType innerType = gt.getFieldType("inner");
		GenericType typeParam = innerType.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", Integer.class, typeParam.getType());
	}

	@Test
	public void testSubclassMethod() {
		// Given:
		Class<?> cls = Subclass.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType innerType = gt.getMethodReturnType("getInner");
		GenericType typeParam = innerType.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then;
		assertEquals("typeParam.getType()", Integer.class, typeParam.getType());
	}

	private static class NonGenericOuter {
		@SuppressWarnings("serial")
		private class Inner extends ArrayList<String> {

		}

		Inner inner;

		@SuppressWarnings("unused")
		public Inner getInner() {
			return inner;
		}
	}

	private static class NonGenericSubclass extends NonGenericOuter {

	}

	@Test
	public void testNonGenericSubclass() {
		// Given:
		Class<?> cls = NonGenericSubclass.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType innerType = gt.getFieldType("inner");
		GenericType typeParam = innerType.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", String.class, typeParam.getType());
	}

	@Test
	public void testNonGenericSubclassMethod() {
		// Given:
		Class<?> cls = NonGenericSubclass.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType innerType = gt.getMethodReturnType("getInner");
		GenericType typeParam = innerType.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", String.class, typeParam.getType());
	}

	private static class DoubleBind<E extends Cloneable & Serializable> {
	}

	@Test
	public void testDoubleBind() {
		// Given:
		Class<?> cls = DoubleBind.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(DoubleBind.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam.getType() instanceof TypeVariable", typeParam.getType() instanceof TypeVariable);
		// When:
		TypeVariable<?> typeVar = (TypeVariable<?>) typeParam.getType();
		Type[] bounds = typeVar.getBounds();
		// Then:
		assertEquals("bounds[0]", Cloneable.class, bounds[0]);
		assertEquals("bounds[1]", Serializable.class, bounds[1]);
	}

	private static interface NumericContainer<T extends Number> {

	}

	private static interface SerializableContainer<T extends Serializable> {

	}

	private static interface DoubleBind2<T extends Number & Serializable> extends NumericContainer<T>,
			SerializableContainer<T> {

	}

	@Test
	public void testDoubleBind2() {
		// Given:
		Class<?> cls = DoubleBind2.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(NumericContainer.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam.getType() instanceof TypeVariable", typeParam.getType() instanceof TypeVariable);
		// When:
		TypeVariable<?> typeVar = (TypeVariable<?>) typeParam.getType();
		Type[] bounds = typeVar.getBounds();
		// Then:
		assertEquals("bounds[0]", Number.class, bounds[0]);
		assertEquals("bounds[1]", Serializable.class, bounds[1]);
	}

	private static interface DoubleBind3<T extends BigDecimal> extends DoubleBind2<T> {

	}

	@Test
	public void testDoubleBind3() {
		// Given:
		Class<?> cls = DoubleBind3.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(NumericContainer.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam.getType() instanceof TypeVariable", typeParam.getType() instanceof TypeVariable);
		// When:
		TypeVariable<?> typeVar = (TypeVariable<?>) typeParam.getType();
		Type[] bounds = typeVar.getBounds();
		// Then:
		assertEquals("bounds.length", 1, bounds.length);
		assertEquals("bounds[0]", BigDecimal.class, bounds[0]);
	}

	@SuppressWarnings("serial")
	private static class ChildStringList extends StringList {

	}

	@Test
	public void testChildStringList() {
		// Given:
		Class<?> cls = ChildStringList.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", String.class, typeParam.getType());
	}

	@SuppressWarnings("serial")
	private static class AnotherStringList extends ArrayList<String> implements Collection<String> {

	}

	@Test
	public void testAnotherStringList() {
		// Given:
		Class<?> cls = AnotherStringList.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam.getType()", String.class, typeParam.getType());

	}

	private static class Pair<K, V> {
		private K key;
		private V value;

		@SuppressWarnings("unused")
		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public Pair() {
		}

		@SuppressWarnings("unused")
		public void setKey(K key) {
			this.key = key;
		}

		@SuppressWarnings("unused")
		public void setValue(V value) {
			this.value = value;
		}

		@SuppressWarnings("unused")
		public K getKey() {
			return key;
		}

		@SuppressWarnings("unused")
		public V getValue() {
			return value;
		}
	}

	private static class IntegerPair extends Pair<Integer, Integer> {

	}

	@Test
	public void testIntegerPair() {
		// Given:
		Class<?> cls = IntegerPair.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam1 = gt.getTypeParameter(Pair.class.getTypeParameters()[0]);
		GenericType typeParam2 = gt.getTypeParameter(Pair.class.getTypeParameters()[1]);
		// Then:
		assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
		assertEquals("typeParam2.getType()", Integer.class, typeParam2.getType());
	}

	private static class StringIntegerPair extends Pair<String, Integer> {
	}

	@Test
	public void testStringIntegerPair() {
		// Given:
		Class<?> cls = StringIntegerPair.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam1 = gt.getTypeParameter(Pair.class.getTypeParameters()[0]);
		GenericType typeParam2 = gt.getTypeParameter(Pair.class.getTypeParameters()[1]);
		// Then:
		assertEquals("typeParam1.getType()", String.class, typeParam1.getType());
		assertEquals("typeParam2.getType()", Integer.class, typeParam2.getType());
	}

	private static class IntegerStringPair extends Pair<Integer, String> {
	}

	@Test
	public void testIntegerStringPair() {
		// Given:
		Class<?> cls = IntegerStringPair.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam1 = gt.getTypeParameter(Pair.class.getTypeParameters()[0]);
		GenericType typeParam2 = gt.getTypeParameter(Pair.class.getTypeParameters()[1]);
		// Then:
		assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
		assertEquals("typeParam2.getType()", String.class, typeParam2.getType());
	}

	private static class BoundedPair<KE extends Number, VE extends Throwable> extends Pair<KE, VE> {
	}

	private static class IntegerErrorPair extends BoundedPair<Integer, Error> {
	}

	@Test
	public void testIntegerErrorPair() {
		// Given:
		Class<?> cls = IntegerErrorPair.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam1 = gt.getTypeParameter(Pair.class.getTypeParameters()[0]);
		GenericType typeParam2 = gt.getTypeParameter(Pair.class.getTypeParameters()[1]);
		// Then:
		assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
		assertEquals("typeParam2.getType()", Error.class, typeParam2.getType());
	}

	@SuppressWarnings("serial")
	private static class YetAnotherStringList extends ArrayList<String> implements SomeCollection<String> {

	}

	@Test
	public void testYetAnotherStringList() {
		// Given:
		Class<?> cls = YetAnotherStringList.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam1 = gt.getTypeParameter(SomeCollection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam1.getType()", String.class, typeParam1.getType());
	}

	public static interface ServiceContainer<S> {
		public S getService();

		public void setService(S s);
	}

	@Test
	public void testServiceContainer() {
		// Given:
		Class<?> cls = ServiceContainer.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam0 = gt.getTypeParameter(ServiceContainer.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam0.isTypeVariable()", typeParam0.isTypeVariable());
		// When:
		Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
		// Then:
		assertEquals("narrowedType", Object.class, narrowedType);
	}

	public static interface ComponentContainer extends ServiceContainer<JComponent> {

	}

	@Test
	public void testComponentContainer() {
		// Given:
		Class<?> cls = ComponentContainer.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam0 = gt.getTypeParameter(ServiceContainer.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam0.isClass()", typeParam0.isClass());
		assertEquals("typeParam0.getType()", JComponent.class, typeParam0.getType());
		// When:
		Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
		// Then:
		assertEquals("narrowedType", JComponent.class, narrowedType);
	}

	@SuppressWarnings("serial")
	private static class FileFilterDialog<T extends FileFilter> extends JDialog implements ServiceContainer<T> {
		private T service;

		public T getService() {
			return service;
		}

		public void setService(T s) {
			service = s;
		}

	}

	@Test
	public void testFileFilterDialog() {
		// Given:
		Class<?> cls = FileFilterDialog.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam0 = gt.getTypeParameter(ServiceContainer.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam0.isTypeVariable()", typeParam0.isTypeVariable());
		// When:
		Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
		// Then:
		assertEquals("narrowedType", FileFilter.class, narrowedType);
	}

	@Test
	public void testFieldInFileFilterDialog() {
		// Given:
		Class<?> cls = FileFilterDialog.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType fieldType = gt.getFieldType("service");
		// Then:
		assertTrue("fieldType.isTypeVariable()", fieldType.isTypeVariable());
		// When:
		Type narrowedType = gt.narrow(fieldType.getType(), Object.class);
		// Then:
		assertEquals("narrowedType", FileFilter.class, narrowedType);
	}

	@Test
	public void testMethodGetServiceInFileFilterDialog() {
		// Given:
		Class<?> cls = FileFilterDialog.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType returnType = gt.getMethodReturnType("getService");
		// Then:
		assertTrue("returnType.isTypeVariable()", returnType.isTypeVariable());
		// When:
		Type narrowedType = gt.narrow(returnType.getType(), Object.class);
		// Then:
		assertEquals("narrowedType", FileFilter.class, narrowedType);
	}

	private static class FileFilterDialog2<T extends JComponent & FileFilter> extends JDialog implements
			ServiceContainer<T> {
		private T service;

		public T getService() {
			return service;
		}

		public void setService(T s) {
			service = s;
		}

	}

	@Test
	public void testFileFilterDialog2() {
		// Given:
		Class<?> cls = FileFilterDialog2.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam0 = gt.getTypeParameter(ServiceContainer.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam0.isTypeVariable()", typeParam0.isTypeVariable());
		// When:
		Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
		// Then:
		assertEquals("narrowedType", JComponent.class, narrowedType);
		// TODO here we need some other API to get both of the given bounds:
		// JComponent & FileFilter

	}

	@Test
	public void testMethodGetServiceInFileFilterDialog2() {
		// Given:
		Class<?> cls = FileFilterDialog2.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType returnType = gt.getMethodReturnType("getService");
		// Then
		assertTrue("returnType.isTypeVariable()", returnType.isTypeVariable());
		// When:
		Type narrowedType = gt.narrow(returnType.getType(), Object.class);
		// Then:
		assertEquals("narrowedType", JComponent.class, narrowedType);
		// TODO here we need some other API to get both of the given bounds:
		// JComponent & FileFilter
	}

	private static class ClassWithStringList {
		@SuppressWarnings("unused")
		List<String> list;
	}

	@Test
	public void testClassWithStringList() {
		// Given:
		Class<?> cls = ClassWithStringList.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType fieldGT = gt.getFieldType("list");
		GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam0.getType()", String.class, typeParam0.getType());

	}

	private static class ClassWithStringListMethod {
		@SuppressWarnings("unused")
		public List<String> getList() {
			return null;
		};
	}

	@Test
	public void testClassWithStringListMethod() {
		// Given:
		Class<?> cls = ClassWithStringListMethod.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType methodGT = gt.getMethodReturnType("getList");
		GenericType typeParam0 = methodGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam0.getType()", String.class, typeParam0.getType());
	}

	private static class ClassWithList {
		@SuppressWarnings({ "unused", "rawtypes" })
		List list;
	}

	@Test
	public void testClassWithList() {
		// Given:
		Class<?> cls = ClassWithList.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType fieldGT = gt.getFieldType("list");
		GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam0.isTypeVariable()", typeParam0.isTypeVariable());
	}
	
	@SuppressWarnings("unused")
	private static class ClassWithListAndWildcard {
		List<?> list;
	}

	@Test
	public void testClassWithListAndWildcard() {
		// Given:
		Class<?> cls = ClassWithListAndWildcard.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType fieldGT = gt.getFieldType("list");
		GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam0.isWildcardType()", typeParam0.isWildcardType());
	}

	private static class ClassWithNumberList<IT extends Number> {
		@SuppressWarnings("unused")
		List<IT> list;
	}

	@Test
	public void testClassWithNumberList() {
		// Given:
		Class<?> cls = ClassWithNumberList.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType fieldGT = gt.getFieldType("list");
		GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertTrue("typeParam0.isTypeVariable()", typeParam0.isTypeVariable());
		// When:
		Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
		// Then:
		assertEquals("narrowedType", Number.class, narrowedType);

	}

	private static class ClassWithConcreteNumberList extends ClassWithNumberList<Long> {
	}

	@Test
	public void testClassWithConcreteNumberList() {
		// Given:
		Class<?> cls = ClassWithConcreteNumberList.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType fieldGT = gt.getFieldType("list");
		GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam0.getType()", Long.class, typeParam0.getType());
	}

	private static class ClassWithMap {
		@SuppressWarnings("unused")
		Map<String, Integer> map;
	}

	@Test
	public void testClassWithMap() {
		// Given:
		Class<?> cls = ClassWithMap.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType fieldGT = gt.getFieldType("map");
		GenericType typeParam0 = fieldGT.getTypeParameter(Map.class.getTypeParameters()[0]);
		GenericType typeParam1 = fieldGT.getTypeParameter(Map.class.getTypeParameters()[1]);
		// Then:
		assertEquals("typeParam0.getType()", String.class, typeParam0.getType());
		assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
	}

	@SuppressWarnings("serial")
	class X12Class extends ArrayList<String> {
		class X13Class extends ArrayList<Integer> {
			class X14Class extends ArrayList<Exception> {

			}
		}
	}

	@Test
	public void testX12Class() {
		// Given:
		Class<?> cls = X12Class.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam0 = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam0.getType()", String.class, typeParam0.getType());
	}

	@Test
	public void testX13Class() {
		// Given:
		Class<?> cls = X12Class.X13Class.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam0 = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam0.getType()", Integer.class, typeParam0.getType());
	}

	@Test
	public void testX14Class() {
		// Given:
		Class<?> cls = X12Class.X13Class.X14Class.class;
		GenericType gt = new GenericType(cls);
		// When:
		GenericType typeParam0 = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
		// Then:
		assertEquals("typeParam0.getType()", Exception.class, typeParam0.getType());
	}
}