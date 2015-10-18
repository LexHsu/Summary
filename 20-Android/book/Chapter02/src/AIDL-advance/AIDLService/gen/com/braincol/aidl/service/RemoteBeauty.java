/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\android\\AIDLService\\src\\com\\braincol\\aidl\\service\\RemoteBeauty.aidl
 */
package com.braincol.aidl.service;
public interface RemoteBeauty extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.braincol.aidl.service.RemoteBeauty
{
private static final java.lang.String DESCRIPTOR = "com.braincol.aidl.service.RemoteBeauty";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.braincol.aidl.service.RemoteBeauty interface,
 * generating a proxy if needed.
 */
public static com.braincol.aidl.service.RemoteBeauty asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.braincol.aidl.service.RemoteBeauty))) {
return ((com.braincol.aidl.service.RemoteBeauty)iin);
}
return new com.braincol.aidl.service.RemoteBeauty.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getAllInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getAllInfo();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getBeauty:
{
data.enforceInterface(DESCRIPTOR);
Beauty _result = this.getBeauty();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.braincol.aidl.service.RemoteBeauty
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public java.lang.String getAllInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAllInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public Beauty getBeauty() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
Beauty _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBeauty, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = Beauty.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getAllInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getBeauty = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public java.lang.String getAllInfo() throws android.os.RemoteException;
public Beauty getBeauty() throws android.os.RemoteException;
}
