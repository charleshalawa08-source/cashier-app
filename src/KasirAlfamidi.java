
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class KasirAlfamidi extends JFrame {

    // Format mata uang Rupiah
    NumberFormat rupiah = NumberFormat.getCurrencyInstance(
            new Locale("id", "ID")
    );

    // Data barang
    class Barang {
        String kode;
        String nama;
        long harga;

        Barang(String kode, String nama, long harga) {
            this.kode = kode;
            this.nama = nama;
            this.harga = harga;
        }

        @Override
        public String toString() {
            return kode + " - " + nama + " - " + rupiah.format(harga);
        }
    }

    ArrayList<Barang> daftarBarang = new ArrayList<>();

    JComboBox<Barang> comboBarang;
    JSpinner jumlahBarang;
    JTextField txtBayar;
    JLabel lblTotal;
    JLabel lblKembalian;

    JTable tabel;
    DefaultTableModel model;
    JTextArea areaStruk;

    long totalBelanja = 0;
    long uangKembali = 0;

    public KasirAlfamidi() {

        setTitle("APLIKASI KASIR ALFAMIDI");
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Data barang contoh
        daftarBarang.add(new Barang("001", "Indomie Goreng", 3500));
        daftarBarang.add(new Barang("002", "Aqua 600ml", 4000));
        daftarBarang.add(new Barang("003", "Beras 5 Kg", 75000));
        daftarBarang.add(new Barang("004", "Minyak Goreng 1L", 18000));
        daftarBarang.add(new Barang("005", "Gula Pasir 1 Kg", 17000));
        daftarBarang.add(new Barang("006", "Susu UHT", 6500));
        daftarBarang.add(new Barang("007", "Roti Tawar", 15000));
        daftarBarang.add(new Barang("008", "Sabun Mandi", 5000));
        daftarBarang.add(new Barang("009", "Teh Botol", 5000));
        daftarBarang.add(new Barang("010", "Telur 1 Kg", 29000));

        // HEADER
        JLabel judul = new JLabel(
                "ALFAMIDI - SISTEM INFORMASI KASIR",
                SwingConstants.CENTER
        );

        judul.setFont(new Font("Arial", Font.BOLD, 25));
        judul.setForeground(Color.WHITE);
        judul.setOpaque(true);
        judul.setBackground(new Color(190, 25, 40));
        judul.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        add(judul, BorderLayout.NORTH);

        // PANEL UTAMA
        JPanel panelUtama = new JPanel(new GridLayout(1, 2, 10, 10));
        panelUtama.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        // PANEL KIRI
        JPanel panelKiri = new JPanel(new BorderLayout(10, 10));
        panelKiri.setBorder(
                BorderFactory.createTitledBorder("TRANSAKSI BARANG")
        );

        // INPUT BARANG
        JPanel panelInput = new JPanel(new GridLayout(3, 2, 8, 8));

        comboBarang = new JComboBox<>();
        for (Barang b : daftarBarang) {
            comboBarang.addItem(b);
        }

        jumlahBarang = new JSpinner(
                new SpinnerNumberModel(1, 1, 100, 1)
        );

        JButton btnTambah = new JButton("TAMBAH BARANG");

        panelInput.add(new JLabel("Pilih Barang"));
        panelInput.add(comboBarang);

        panelInput.add(new JLabel("Jumlah Barang"));
        panelInput.add(jumlahBarang);

        panelInput.add(new JLabel(""));
        panelInput.add(btnTambah);

        panelKiri.add(panelInput, BorderLayout.NORTH);

        // TABEL BARANG
        model = new DefaultTableModel(
                new Object[]{
                    "Kode", "Nama Barang", "Harga",
                    "Jumlah", "Subtotal"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        tabel.setRowHeight(25);

        JScrollPane scrollTabel = new JScrollPane(tabel);
        panelKiri.add(scrollTabel, BorderLayout.CENTER);

        // TOMBOL HAPUS
        JButton btnHapus = new JButton("HAPUS BARANG");
        JButton btnKosong = new JButton("KOSONGKAN");

        JPanel panelTombol = new JPanel();
        panelTombol.add(btnHapus);
        panelTombol.add(btnKosong);

        panelKiri.add(panelTombol, BorderLayout.SOUTH);

        // PANEL KANAN
        JPanel panelKanan = new JPanel(new BorderLayout(8, 8));
        panelKanan.setBorder(
                BorderFactory.createTitledBorder("PEMBAYARAN DAN STRUK")
        );

        // PANEL PEMBAYARAN
        JPanel panelBayar = new JPanel(new GridLayout(4, 2, 8, 10));

        lblTotal = new JLabel("Rp0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);

        txtBayar = new JTextField();

        lblKembalian = new JLabel("Rp0");
        lblKembalian.setForeground(new Color(0, 120, 0));
        lblKembalian.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnBayar = new JButton("PROSES BAYAR");
        JButton btnPrint = new JButton("CETAK STRUK");

        panelBayar.add(new JLabel("Total Belanja"));
        panelBayar.add(lblTotal);

        panelBayar.add(new JLabel("Uang Pembayaran"));
        panelBayar.add(txtBayar);

        panelBayar.add(new JLabel("Uang Kembalian"));
        panelBayar.add(lblKembalian);

        panelBayar.add(btnBayar);
        panelBayar.add(btnPrint);

        panelKanan.add(panelBayar, BorderLayout.NORTH);

        // AREA STRUK
        areaStruk = new JTextArea();
        areaStruk.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaStruk.setEditable(false);

        JScrollPane scrollStruk = new JScrollPane(areaStruk);
        scrollStruk.setBorder(
                BorderFactory.createTitledBorder("PREVIEW STRUK BELANJA")
        );

        panelKanan.add(scrollStruk, BorderLayout.CENTER);

        panelUtama.add(panelKiri);
        panelUtama.add(panelKanan);

        add(panelUtama, BorderLayout.CENTER);

        // AKSI TOMBOL TAMBAH
        btnTambah.addActionListener(e -> tambahBarang());

        // AKSI HAPUS BARANG
        btnHapus.addActionListener(e -> hapusBarang());

        // AKSI KOSONGKAN
        btnKosong.addActionListener(e -> kosongkan());

        // AKSI PEMBAYARAN
        btnBayar.addActionListener(e -> prosesBayar());

        // AKSI CETAK
        btnPrint.addActionListener(e -> cetakStruk());

        // Awal tampilan
        areaStruk.setText(
                "       ALFAMIDI MINI MARKET\n" +
                "     SISTEM KASIR SEDERHANA\n" +
                "--------------------------------\n" +
                " Silakan masukkan barang belanja\n" +
                "--------------------------------\n"
        );
    }

    // MENAMBAHKAN BARANG
    private void tambahBarang() {

        Barang barang = (Barang) comboBarang.getSelectedItem();

        if (barang == null) {
            return;
        }

        int jumlah = (int) jumlahBarang.getValue();

        boolean ditemukan = false;

        for (int i = 0; i < model.getRowCount(); i++) {

            String kode = model.getValueAt(i, 0).toString();

            if (kode.equals(barang.kode)) {

                int jumlahLama = Integer.parseInt(
                        model.getValueAt(i, 3).toString()
                );

                int jumlahBaru = jumlahLama + jumlah;

                model.setValueAt(jumlahBaru, i, 3);

                model.setValueAt(
                        rupiah.format(barang.harga * jumlahBaru),
                        i, 4
                );

                ditemukan = true;
                break;
            }
        }

        if (!ditemukan) {
            model.addRow(new Object[]{
                barang.kode,
                barang.nama,
                rupiah.format(barang.harga),
                jumlah,
                rupiah.format(barang.harga * jumlah)
            });
        }

        hitungTotal();
    }

    // MENGHITUNG TOTAL BELANJA
    private void hitungTotal() {

        totalBelanja = 0;

        for (int i = 0; i < model.getRowCount(); i++) {

            int jumlah = Integer.parseInt(
                    model.getValueAt(i, 3).toString()
            );

            Barang barang = cariBarang(
                    model.getValueAt(i, 0).toString()
            );

            if (barang != null) {
                totalBelanja += barang.harga * jumlah;
            }
        }

        lblTotal.setText(rupiah.format(totalBelanja));
    }

    // MENCARI DATA BARANG
    private Barang cariBarang(String kode) {

        for (Barang b : daftarBarang) {
            if (b.kode.equals(kode)) {
                return b;
            }
        }

        return null;
    }

    // MENGHAPUS BARANG
    private void hapusBarang() {

        int baris = tabel.getSelectedRow();

        if (baris >= 0) {
            model.removeRow(baris);
            hitungTotal();
        } else {
            JOptionPane.showMessageDialog(
                    this, "Pilih barang yang ingin dihapus!"
            );
        }
    }

    // KOSONGKAN TRANSAKSI
    private void kosongkan() {

        model.setRowCount(0);
        totalBelanja = 0;
        uangKembali = 0;

        lblTotal.setText("Rp0");
        lblKembalian.setText("Rp0");
        txtBayar.setText("");

        areaStruk.setText(
                "       ALFAMIDI MINI MARKET\n" +
                "--------------------------------\n" +
                " Silakan masukkan barang belanja\n"
        );
    }

    // PROSES PEMBAYARAN
    private void prosesBayar() {

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this, "Belum ada barang yang dibeli!"
            );
            return;
        }

        try {

            String input = txtBayar.getText().trim();

            if (!input.matches("[0-9]+")) {
                JOptionPane.showMessageDialog(
                        this, "Masukkan pembayaran berupa angka!"
                );
                return;
            }

            long uangBayar = Long.parseLong(input);

            if (uangBayar < totalBelanja) {
                JOptionPane.showMessageDialog(
                        this, "Uang pembayaran tidak mencukupi!"
                );
                return;
            }

            uangKembali = uangBayar - totalBelanja;

            lblKembalian.setText(rupiah.format(uangKembali));

            buatStruk(uangBayar);

            JOptionPane.showMessageDialog(
                    this,
                    "Pembayaran berhasil!\n" +
                    "Kembalian: " + rupiah.format(uangKembali)
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Nominal pembayaran terlalu besar!"
            );
        }
    }

    // MEMBUAT STRUK BELANJA
    private void buatStruk(long uangBayar) {

        StringBuilder struk = new StringBuilder();

        struk.append("       ALFAMIDI MINI MARKET\n");
        struk.append("     STRUK PEMBAYARAN KASIR\n");
        struk.append("--------------------------------\n");

        struk.append("Tanggal : ").append(
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                        .format(new Date())
        ).append("\n");

        struk.append("Kasir   : Admin\n");
        struk.append("--------------------------------\n");

        for (int i = 0; i < model.getRowCount(); i++) {

            String nama = model.getValueAt(i, 1).toString();

            int jumlah = Integer.parseInt(
                    model.getValueAt(i, 3).toString()
            );

            Barang b = cariBarang(
                    model.getValueAt(i, 0).toString()
            );

            if (b != null) {

                long subtotal = b.harga * jumlah;

                struk.append(nama).append("\n");

                struk.append(jumlah)
                     .append(" x ")
                     .append(rupiah.format(b.harga))
                     .append("\n");

                struk.append("Subtotal: ")
                     .append(rupiah.format(subtotal))
                     .append("\n");
            }
        }

        struk.append("--------------------------------\n");

        struk.append("TOTAL     : ")
             .append(rupiah.format(totalBelanja))
             .append("\n");

        struk.append("BAYAR     : ")
             .append(rupiah.format(uangBayar))
             .append("\n");

        struk.append("KEMBALIAN : ")
             .append(rupiah.format(uangKembali))
             .append("\n");

        struk.append("--------------------------------\n");
        struk.append("     TERIMA KASIH TELAH BELANJA\n");
        struk.append("          DI ALFAMIDI\n");
        struk.append("--------------------------------\n");

        areaStruk.setText(struk.toString());
    }

    // CETAK STRUK KE PRINTER
    private void cetakStruk() {

        if (model.getRowCount() == 0 ||
                areaStruk.getText().isEmpty() ||
                !areaStruk.getText().contains("KEMBALIAN")) {

            JOptionPane.showMessageDialog(
                    this, "Silakan proses pembayaran terlebih dahulu!"
            );

            return;
        }

        try {

            boolean selesai = areaStruk.print();

            if (selesai) {
                JOptionPane.showMessageDialog(
                        this, "Struk berhasil dikirim ke printer!"
                );
            }

        } catch (PrinterException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal mencetak struk: " + ex.getMessage()
            );
        }
    }

    // MAIN PROGRAM
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new KasirAlfamidi().setVisible(true);

        });
    }
}
