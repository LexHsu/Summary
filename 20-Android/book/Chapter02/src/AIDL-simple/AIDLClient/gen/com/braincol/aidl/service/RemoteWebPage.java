/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\android\\AIDLClient\\src\\com\\braincol\\aidl\\service\\RemoteWebPage.aidl
 */
package com.braincol.aidl.service;
public interface RemoteWebPage extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.braincol.aidl.service.RemoteWebPage
{
private static final java.lang.String DESCRIPTOR = "com.braincol.aidl.service.RemoteWebPage";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.braincol.aidl.service.RemoteWebPage interface,
 * generating a proxy if needed.
 */
public static com.braincol.aidl.service.RemoteWebPage asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.braincol.aidl.service.RemoteWebPage))) {
return ((com.braincol.aidl.service.RemoteWebPage)iin);
}
return new com.braincol.aidl.service.RemoteWebPage.Stub.Proxy(obj);
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
case TRANSACTION_getCurrentPageUrl:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getCurrentPageUrl();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.braincol.aidl.service.RemoteWebPage
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
public java.lang.String getCurrentPageUrl() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentPageUrl, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getCurrentPageUrl = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public java.lang.String getCurrentPageUrl() throws android.os.RemoteException;
}
