package com.parallelcodes.mylauncher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class HomeActivity extends Activity {
    TextView txtTime, txtDate;
    Calendar c;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;

    PackageManager packageManager;
    public static List<AppInfo> apps;
    GridView grdView;
    public static ArrayAdapter<AppInfo> adapter;

    LinearLayout containAppDrawer;

    RelativeLayout ContainerHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("HH:mm aa");
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDate = (TextView) findViewById(R.id.txtDate);

        containAppDrawer = (LinearLayout) findViewById(R.id.containAppDrawer);
        ContainerHome = (RelativeLayout) findViewById(R.id.ContainerHome);
        HideAppDrawer(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        c = Calendar.getInstance();
                        txtDate.setText(simpleDateFormat.format(c.getTime()));
                        txtTime.setText(simpleTimeFormat.format(c.getTime()));
                    }

                });

            }
        }, 0, 10);
        apps = null;
        adapter = null;
        loadApps();
        loadListView();
        addGridListeners();

    }

    public void addGridListeners() {
        try {
            grdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = packageManager.getLaunchIntentForPackage(apps.get(i).name.toString());
                    HomeActivity.this.startActivity(intent);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(HomeActivity.this, ex.getMessage().toString() + " grade", Toast.LENGTH_LONG).show();
            Log.e("Erro grade", ex.getMessage().toString() + " grade");
        }

    }


    private void loadListView() {

        try {
            grdView = (GridView) findViewById(R.id.grd_allApps);
            if (adapter == null) {
                adapter = new ArrayAdapter<AppInfo>(this, R.layout.grd_items, apps) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        ViewHolderItem viewHolder = null;

                        if (convertView == null) {
                            convertView = getLayoutInflater().inflate(
                                    R.layout.grd_items, parent, false
                            );
                            viewHolder = new ViewHolderItem();
                            viewHolder.icon = (ImageView) convertView.findViewById(R.id.img_icon);
                            viewHolder.name = (TextView) convertView.findViewById(R.id.txt_name);
                            viewHolder.label = (TextView) convertView.findViewById(R.id.txt_label);

                            convertView.setTag(viewHolder);
                        } else {
                            viewHolder = (ViewHolderItem) convertView.getTag();
                        }

                        AppInfo appInfo = apps.get(position);

                        if (appInfo != null) {
                            viewHolder.icon.setImageDrawable(appInfo.icon);
                            viewHolder.label.setText(appInfo.label);
                            viewHolder.name.setText(appInfo.name);
                        }
                        return convertView;

                    }

                    final class ViewHolderItem {
                        ImageView icon;
                        TextView label;
                        TextView name;
                    }
                };
            }

            grdView.setAdapter(adapter);
        } catch (Exception ex) {
            Toast.makeText(HomeActivity.this, ex.getMessage().toString() + " loadListView", Toast.LENGTH_LONG).show();
            Log.e("Error loadListView", ex.getMessage().toString() + " loadListView");
        }

    }

    int numTela = 0;
    @SuppressLint("LongLogTag")
    private void loadApps() {
        try {
            if (packageManager == null)
                packageManager = getPackageManager();
            if (apps == null) {
                apps = new ArrayList<AppInfo>();

                Intent i = new Intent(Intent.ACTION_MAIN, null);
                i.addCategory(Intent.CATEGORY_LAUNCHER);

                //aqui listagem dos apps, colocar paginacao
                List<ResolveInfo> availableApps = packageManager.queryIntentActivities(i, 0);
                List<ResolveInfo> appTela = new ArrayList<>();

                List<ResolveInfo> ListaAplicativos = new String[]{"ContactsContract.CommonDataKinds.Phone",
                        "com.android.camera2.com.android.chrome",
                        "com.android.camera.CameraActivity",
                        "org.chromium.chrome.browser.ChromeTabbedActivity",
                        "com.android.contacts.activities.PeopleActivity"};
                //com.android.camera2, com.android.chrome,
                //com.android.camera.CameraActivity
                //org.chromium.chrome.browser.ChromeTabbedActivity
                //com.android.contacts.activities.PeopleActivity
                //

                int qtdApp = availableApps.size();
                for(int x=0;x<=qtdApp;x++){

                    if(ListaAplicativos.get(x) == availableApps.get(int a){
                        Object e1 = null;
                        appTela.add(availableApps.add(a, ResolveInfo e1));

                    }

                }

                //usar appTela (conforme a pagina vai ser o numero de 0-8/9-17/18-26)
                    int numInicial = (9*numTela);
                    int numFinal = (8+numInicial);

                    for(int y=numInicial;y<numFinal;y++) {
                        //for (ResolveInfo ri : availableApps) {
                        for (ResolveInfo ri : appTela) {
                            AppInfo appinfo = new AppInfo();
                            appinfo.label = ri.loadLabel(packageManager);
                            appinfo.name = ri.activityInfo.packageName;
                            appinfo.icon = ri.activityInfo.loadIcon(packageManager);
                            apps.add(appinfo);


                            //availableApps.size(); //9 por tela
                            //variavel local para saber qual tela estou
                            //varrer a lista conforme o layout da tela que esta e reinderizar

                        }
                    }
                }


        } catch (Exception ex) {
            Toast.makeText(HomeActivity.this, ex.getMessage().toString() + " Erro geração aplicativos", Toast.LENGTH_LONG).show();
            Log.e("Erro geração aplicativos", ex.getMessage().toString() + " Erro geração aplicativos");
        }

    }

    ImageView setaDireita = (ImageView) findViewById(R.id.setaDireita);
    ImageView setaEsquerda = (ImageView) findViewById(R.id.setaEsquerda);
    ImageView paginaInicial = (ImageView) findViewById(R.id.paginaInicial);
    Button btSetaDireita = (Button) findViewById(R.id.btSetaDireita);
    Button btPaginaInicial = (Button) findViewById(R.id.btPaginaInicial);
    Button btSetaEsquerda = (Button) findViewById(R.id.btSetaEsquerda);
    private final Object view = new View();

//    btSetaDireita.setOnClickListener(new View.setOnClickListener() {
//        public void onClick(View view) {
//            alterarNumeroTela(numTela, "Soma");
//        }
//        });

    private void setaDireita(View v) {
        alterarNumeroTela(numTela, "Soma");
    }

//    btPaginaInicial.setOnClick(new View.OnClick() {
//        public void onClick(View view) {
//            alterarNumeroTela(numTela, "Inicial");
//        }
//    });

    private void paginaInicial(View v) {
        alterarNumeroTela(numTela, "Inicial");
    }


//    btSetaEsquerda.setOnClick(new View.OnClick() {
//         public void onClick(View view) {
//            alterarNumeroTela(numTela, "Subtrair");
//        }
//    });

    private void setaEsquerda(View v) {
        alterarNumeroTela(numTela, "Subtrair");
    }

    //criar metodo para incrementar alterarNumeroTela se recebe voltar descremeta e avancar incrementa e home volta 0
    public int alterarNumeroTela(int numeroTela, String operacao){

        if(operacao == "Soma"){
            numeroTela = numeroTela + 1;
        }
        if(operacao == "Inicial"){
            numeroTela = 0;
        }
        else{
            numeroTela = numeroTela - 1;
        }
        return numeroTela;
    }

    public void showApps(View v) {
        // Intent i = new Intent(HomeActivity.this, GetApps.class);
        //startActivity(i);
        HideAppDrawer(true);
    }

    public void HideAppDrawer(Boolean visibility) {
        if (visibility) {
            containAppDrawer.setVisibility(View.VISIBLE);
            ContainerHome.setVisibility(View.GONE);
        } else {
            containAppDrawer.setVisibility(View.GONE);
            ContainerHome.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        HideAppDrawer(false);
    }
}
