package com.appspot.fontheightdb.models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Font {
  public static Font fromJsonString(String jsonString) {
    JSONObject obj = (JSONObject) JSONValue.parse(jsonString);
    String name = (String) obj.get("name");
    Font font = new Font(name);
    
    JSONArray sizes = (JSONArray) obj.get("sizes");
    JSONArray heights = (JSONArray) obj.get("heights");
    JSONArray ascents = (JSONArray) obj.get("ascents");
    JSONArray descents = (JSONArray) obj.get("descents");
    for (int i = 0; i < sizes.size(); i++) {
      font.addMetric(
          ((Number) sizes.get(i)).intValue(),
          ((Number) heights.get(i)).intValue(),
          ((Number) ascents.get(i)).intValue(),
          ((Number) descents.get(i)).intValue()
      );
    }

    return font;
  }

  @Persistent
  private List<Integer> ascents;

  @Persistent
  private List<Integer> descents;

  @Persistent
  private List<Integer> heights;

  @PrimaryKey
  private String name;

  @Persistent
  private List<Integer> sizes;
  
  public Font(String name) {
    super();
    this.name = name;
    ascents = new ArrayList<Integer>();
    descents = new ArrayList<Integer>();
    heights = new ArrayList<Integer>();
    sizes = new ArrayList<Integer>();
  }

  public void addMetric(int size, int height, int ascent, int descent) {
    sizes.add(size);
    heights.add(height);
    ascents.add(ascent);
    descents.add(descent);
  }

  public String getName() {
    return name;
  }

  public String toJsonString() {
    JSONObject obj = new JSONObject();
    obj.put("name", name);
    JSONArray sizes = new JSONArray();
    JSONArray heights = new JSONArray();
    JSONArray ascents = new JSONArray();
    JSONArray descents = new JSONArray();
    for (int i = 0; i < this.sizes.size(); i++) {
      sizes.add(this.sizes.get(i));
      heights.add(this.heights.get(i));
      ascents.add(this.ascents.get(i));
      descents.add(this.descents.get(i));
    }
    obj.put("sizes", sizes);
    obj.put("heights", heights);
    obj.put("ascents", ascents);
    obj.put("descents", descents);
    return obj.toJSONString();
  }
}
