package br.ufc.great.loccamlib;

import br.ufc.great.syssu.base.interfaces.ISysSUService;

public interface LoccamListener {
	public void onServiceConnected(ISysSUService service);
	public void onServiceDisconnected();
	public void onLoccamException(Exception ex);
}
