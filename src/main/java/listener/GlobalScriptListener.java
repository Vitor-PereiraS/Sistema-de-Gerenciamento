package listener;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class GlobalScriptListener  implements SystemEventListener{

	@Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot view = context.getViewRoot();

        // Verifica se o script já foi adicionado nesta tela para evitar duplicação
        if (view != null && view.findComponent("globalDatatableScript") == null) {
            UIOutput script = new UIOutput();
            script.setId("globalDatatableScript");
            script.setRendererType("javax.faces.resource.Script");
            script.getAttributes().put("library", "js");
            script.getAttributes().put("name", "datatable-click.js");

            // Adiciona exatamente no final do body, igual ao target="body"
            view.addComponentResource(context, script, "body");
        }
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }
}