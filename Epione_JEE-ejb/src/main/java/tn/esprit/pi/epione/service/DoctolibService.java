package tn.esprit.pi.epione.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import tn.esprit.pi.epione.iservices.DoctolibServiceLocal;
import tn.esprit.pi.epione.persistence.Acts;
import tn.esprit.pi.epione.persistence.Adresse;
import tn.esprit.pi.epione.persistence.Doctor;
import tn.esprit.pi.epione.persistence.DoctorFormation;
import tn.esprit.pi.epione.persistence.Pattern;
import tn.esprit.pi.epione.persistence.Speciality;

@Stateless
public class DoctolibService implements DoctolibServiceLocal {

	DoctolibServiceLocal DL;

	@Override
	public Doctor get(String path) {

		String name;
		String url = path;
		try {
			Document doc = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
					.get();
			String title = doc.title();
			Doctor d = new Doctor();
			List<DoctorFormation> DF = new ArrayList<>();

			List<Pattern> list_patterns = new ArrayList<>();
			List<Acts> list_acts = new ArrayList<>();

			/* les elements */
			Element CompleteName = doc.select("span[itemprop=\"name\"]").first(); // Complete Name
			Element Speciality = doc.select("h2.dl-profile-header-speciality").first(); // Speciality
			Element Img = doc.select("img[itemprop=\"image\"][src]").first(); // Profile Picture
			String urlImg = Img.absUrl("src"); // Profile Picture link
			Element Adresse = doc.select("div.dl-profile-text").get(2); // Adresse
			Element bio = doc.select("div.dl-profile-text.js-bio.dl-profile-bio").first();
			d.setBiography(bio.text());
			d.setDoctolib(path);
			d.setOfficeAdress(Adresse.text());
			d.setPicture(urlImg);

			/* set speciality */
			Speciality s = new Speciality();
			s.setSpeciality(Speciality.text());
			d.setSpeciality(s);
			/* set firstname lastname from complete name */
			if (CompleteName.text().split("\\w+").length > 1) {

				d.setLastname(CompleteName.text().substring(CompleteName.text().lastIndexOf(" ") + 1));
				d.setFirstname(CompleteName.text().substring(0, CompleteName.text().lastIndexOf(' ')));
			} else {
				d.setFirstname(CompleteName.text());
			}

			/* remboursement */
			String remboursement = null;
			Element remb = doc.select("div.dl-profile-text").first();

			/*
			 * for (Element e : remb) { if
			 * (e.text().equalsIgnoreCase("Tarifs et remboursements ")) {
			 * 
			 * remboursement = e.nextElementSibling().text(); } }
			 */
			remboursement = remb.text().replace("Voir les tarifs", "");

			d.setRemboursement(remboursement);
			/* end remboursement */

			/* Payment Method */
			String PaymentMethod = null;
			Elements Payment = doc.select("h3.dl-profile-card-subtitle");

			for (Element e : Payment) {
				if (e.text().equalsIgnoreCase("Moyens de paiement")) {

					PaymentMethod = e.nextElementSibling().text();
				}
			}
			/* end Payment Method */

			d.setPaymentMethod(PaymentMethod);

			/* liste formations et distinctions */
			Elements Formation = doc.select("div.dl-profile-entry");

			for (Element f : Formation) {
				if (f.text().equals("Exercices en cabinet")) {
					break;
				}
				String date = f.select("div.dl-profile-entry-time").text();
				String label = f.select("div.dl-profile-entry-label").text();
				if (!(date.contains("-") == true || date.contains("Depuis") == true)) {
					DoctorFormation formation = new DoctorFormation(date, label, d);
					DF.add(formation);
					System.out.println(date);
				}

				/*
				 * DoctorFormation formation = new
				 * DoctorFormation(f.select("div.dl-profile-entry-time").text(),
				 * f.select("div.dl-profile-entry-label").text(), d);
				 */

			}

			d.setFormations(DF);

			/* end formations et distinctions */

			/* scrap motifs and add to doctor */
			Elements Act = doc.select("div.dl-profile-skill-chip");

			for (Element f : Act) {

				Acts a = new Acts(f.text(), d);
				System.out.println(a.getName());
				list_acts.add(a);
			}
			d.setActs(list_acts);

			Elements Motif = doc.select("option");

			for (Element f : Motif) {

				Pattern p = new Pattern(f.text(), d);
				System.out.println(p.getLabel());
				list_patterns.add(p);
			}
			d.setPatterns(list_patterns);

			/* end motifs */

			return d;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<Doctor> getDoctorsbySpeciality(String speciality, int page) {
		String url = "https://www.doctolib.fr/" + speciality + "?page=" + page;
		if (page == 0) {
			url = "https://www.doctolib.fr/" + speciality + "?page=" + 1;

		}

		Document doc;
		List<Doctor> liste_doc = new ArrayList<>();

		try {
			doc = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
					.get();

			Elements paragraphs = doc.getElementsByClass("dl-search-result-presentation");
			for (Element p : paragraphs) {

				String name = p.select(".dl-search-result-name").text();
				String adresse = p.select(".dl-text").text();
				String image = p.select("img").attr("src");
				String remboursement = p.select("div.dl-search-result-regulation-sector").text();
				String spec = p.select("div.dl-search-result-subtitle").text();
				Element link = p.select("a.dl-search-result-name").first();
				String doctolib = link.absUrl("href");

				Doctor doctor = new Doctor();
				doctor.setDoctolib(doctolib);
				doctor.setFirstname(name);
				doctor.setOfficeAdress(adresse);
				doctor.setPicture(image);
				doctor.setRemboursement(remboursement);

				Speciality s = new Speciality();
				s.setSpeciality(spec);
				doctor.setSpeciality(s);

				liste_doc.add(doctor);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return liste_doc;
	}

	/* Get Json from Doctolib page */
	@Override
	public String getFromJson(String path, String page) {
		String js = "";
		// String ur="https://www.doctolib.fr/";
		StringBuilder stringBuilder = new StringBuilder();
		String ur = "https://www.doctolib.fr/";
		if(page==null){
			page = "0";
			}	
		try {
			
			if (Integer.parseInt(page) != 0) {
				ur = "https://www.doctolib.fr/" + path + ".json?page=" + page;
			} else {
				ur = "https://www.doctolib.fr/" + path + ".json";
			}

			// URLConnection url = new
			// URL("https://www.doctolib.fr/"+path+".json?page="+page).openConnection();
			URLConnection url = new URL(ur).openConnection();

			url.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
			String str = "";

			while (null != (str = br.readLine())) {
				System.out.println(str);
				stringBuilder.append(str);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return stringBuilder.toString();
	}

}
