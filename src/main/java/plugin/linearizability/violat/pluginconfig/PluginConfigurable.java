package plugin.linearizability.violat.pluginconfig;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import plugin.linearizability.violat.toolwindows.SettingsForm;

import javax.swing.*;

public class PluginConfigurable implements Configurable {
    private GlobalSettings settings;
    private SettingsForm form;

    PluginConfigurable(GlobalSettings settings) {
        this.settings = settings;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Violat";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if(this.form == null) {
            this.form = new SettingsForm();
            reset();
        }
        return this.form.getMainPanel();
    }

    @Override
    public boolean isModified() {
        if(this.form != null) return this.form.isModified();
        return false;
    }

    @Override
    public void apply() {
        if(this.form == null) return;
        this.form.setModified(false);
//        this.settings.setShowConsole(this.form.isShowConsole());
    }

    @Override
    public void reset() {
//        this.form.setShowConsole(settings.isShowConsole());
        this.form.setModified(false);
    }
}
