package com.ratemyapp.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Hashtable;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

public class StorageHelper
{
	static final String RECORDSTORE_ID = "ratemyapp_settings";
	
	private Hashtable settings = null;
	
	private RecordStore recordStore = null;
	
	public StorageHelper()
	{
		try
		{
			loadSettings();
		}
		catch(Exception e)
		{
			this.settings = new Hashtable();
		}
	}
	
	public boolean storeSetting(String key, String value, boolean overwrite) throws Exception
	{
		if(settings.containsKey(key) && !overwrite)
			return false;
		
		byte[] valueBytes = null;
		
		ByteArrayOutputStream os = null;
		DataOutputStream dos = null;
		
		try
		{
			os = new ByteArrayOutputStream();
			dos = new DataOutputStream(os);
			
			dos.writeUTF(key);
			dos.writeUTF(value);
			
			valueBytes = os.toByteArray();
		}
		catch(Exception e)
		{
		}
		finally
		{
			try
			{
				if(dos != null)
					dos.close();
				if(os != null)
					os.close();
			}
			catch(Exception e)
			{
			}
		}
		
		if(valueBytes != null)
		{
			startRecordStoreOperation();
			
			if(settings.containsKey(key))
			{
				Setting setting = (Setting)settings.get(key);
				
				setting.setValue(value);
				
				recordStore.setRecord(setting.getRecordStoreID(), valueBytes, 0, valueBytes.length);
			}
			else
			{
				int recordStoreID = recordStore.addRecord(valueBytes, 0, valueBytes.length);
				
				settings.put(key, new Setting(recordStoreID, value));
			}
			
			finishRecordStoreOperation();
		}
		return true;
	}
	public String getSetting(String key)
	{
		if(settings.containsKey(key))
		{
			return ((Setting)settings.get(key)).getValue();
		}
		return null;
	}
	public void removeSetting(String key) throws Exception
	{
		if(settings.containsKey(key))
		{
			startRecordStoreOperation();
			
			recordStore.deleteRecord(((Setting)settings.get(key)).getRecordStoreID());
			
			finishRecordStoreOperation();
			
			settings.remove(key);
		}
	}
	
	private void loadSettings() throws Exception
	{
		settings = new Hashtable();
		
		startRecordStoreOperation();
		
		RecordEnumeration enumeration = recordStore.enumerateRecords(null, null, false);
		
		while(enumeration.hasNextElement())
		{
			int recordStoreID = enumeration.nextRecordId();
			
			byte[] bytes = recordStore.getRecord(recordStoreID);
			
			ByteArrayInputStream is = new ByteArrayInputStream(bytes);
			DataInputStream dis = new DataInputStream(is);
			
			String key = dis.readUTF();
			String value = dis.readUTF();
			
			settings.put(key, new Setting(recordStoreID, value));
			
			dis.close();
			is.close();
		}
		
		
		finishRecordStoreOperation();
	}
	private void startRecordStoreOperation() throws Exception
	{
		recordStore = RecordStore.openRecordStore(RECORDSTORE_ID, true);
	}
	private void finishRecordStoreOperation() throws Exception
	{
		recordStore.closeRecordStore();
	}
	
	private static class Setting
	{
		private int recordStoreID;
		private String value;
		
		public Setting(int recordStoreID, String value)
		{
			this.recordStoreID = recordStoreID;
			this.value = value;
		}
		public int getRecordStoreID()
		{
			return recordStoreID;
		}
		public String getValue()
		{
			return value;
		}
		public void setValue(String value)
		{
			this.value = value;
		}
	}
}
