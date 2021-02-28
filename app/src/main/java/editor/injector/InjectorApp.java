package editor.injector;

import com.google.inject.Injector;

public final class InjectorApp {

    private static Injector injector;

    public static void SetInjector(Injector inj){
        injector = inj;
    }
    public static Injector getInjector(){
      return injector;
    }
}
