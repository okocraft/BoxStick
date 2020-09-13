package net.okocraft.boxstick;

import java.util.List;
import java.util.Objects;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.okocraft.boxstick.listener.FarmerStickListener;
import net.okocraft.boxstick.listener.GUIListener;
import net.okocraft.boxstick.listener.WithdrawStickListener;
import net.okocraft.boxstick.sticks.Stick;
import net.okocraft.boxstick.sticks.stickdata.StickType;

/**
 * memo: 情報はItemStackに保存 -> 達成 コマンドは棒を与える/boxstickコマンドのみ。
 * 棒をメインハンドに持って右クリックすることで、棒の設定を開く。
 * 棒の設定で某自体の種類も変更可能にする。棒の名前はいじらない（変えても使える）。
 * 棒の設定では棒の種類自体も変えられるようにする。選択されている種類のアイコンを光らす。
 * 
 */
public class BoxStick extends JavaPlugin {

    @Override
    public void onEnable() {

        // コマンド実装
        PluginCommand boxStickCommand = Objects.requireNonNull(getCommand("boxstick"), "plugin.yml is invalid.");
        boxStickCommand.setTabCompleter((sender, command, label, args) -> List.of());
        boxStickCommand.setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player && sender.hasPermission("boxstick.get")) {
                Player player = (Player) sender;
                player.getInventory().addItem(new Stick(StickType.WITHDRAW).getItem().getItemStack());
            }
            return true;
        });

        new FarmerStickListener(this).start();
        new WithdrawStickListener(this).start();
        new GUIListener(this).start();

        getLogger().info(getName() + " have been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(getName() + " have been disabled!");
    }
}
